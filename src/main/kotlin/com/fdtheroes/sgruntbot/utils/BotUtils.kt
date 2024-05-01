package com.fdtheroes.sgruntbot.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.StreamSupport
import kotlin.random.Random

@Service
class BotUtils(private val botConfig: BotConfig) : DefaultAbsSender(botConfig.defaultBotOptions, botConfig.token) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun isMessageInChat(message: Message): Boolean {
        return message.chatId?.toString() == botConfig.chatId
    }

    fun getUserName(user: User?): String {
        if (user == null) {
            return ""
        }
        val name = if (!user.userName.isNullOrEmpty()) user.userName else user.firstName
        return name
    }

    fun getUserLink(user: User?): String {
        if (user == null) {
            return ""
        }
        val name = getUserName(user)
        return """<a href="tg://user?id=${user.id}">${name}</a>"""
    }

    fun streamFromURL(
        url: String,
        params: String? = null,
        headers: List<Pair<String, String>> = emptyList(),
    ): InputStream {
        return URL(String.format(url, params))
            .openConnection(botConfig.proxy)
            .apply { headers.forEach { setRequestProperty(it.first, it.second) } }
            .getInputStream()
    }

    fun textFromURL(url: String, params: String? = null, headers: List<Pair<String, String>> = emptyList()): String {
        return streamFromURL(url, params, headers)
            .readAllBytes()
            .decodeToString()
    }

    fun shutdown() {
        this.exe.shutdown()
    }

    fun rispondi(actionMessage: ActionResponse, message: Message) {
        when (actionMessage.type) {
            ActionResponseType.Message -> rispondiMessaggio(message, actionMessage.message)
            ActionResponseType.Photo -> rispondiPhoto(message, actionMessage.message, actionMessage.inputFile!!)
            ActionResponseType.Audio -> rispondiAudio(message, actionMessage.inputFile!!)
        }
    }

    fun messaggio(actionMessage: ActionResponse) {
        when (actionMessage.type) {
            ActionResponseType.Message -> messaggio(actionMessage.message)
            ActionResponseType.Photo -> photo(actionMessage.message, actionMessage.inputFile!!)
            ActionResponseType.Audio -> audio(actionMessage.inputFile!!)
        }
    }

    fun getChatMember(userId: Long): User? {
        val getChatMember = GetChatMember().apply {
            this.chatId = botConfig.chatId
            this.userId = userId
        }
        val chatMember = try {
            execute(getChatMember)
        } catch (e: Exception) {
            log.error("Problema con l'utente $userId", e)
            return null
        }
        if (chatMember == null) {
            return null
        }
        if (chatMember.status == "kicked") {
            return null
        }
        return chatMember.user
    }

    fun sleep(seconds: IntRange) {
        Thread.sleep(Random.nextLong(seconds.first.toLong() * 1000, seconds.last.toLong() * 1000))
    }

    fun trimString(input: String, length: Int): String {
        if (input.length < length) {
            return input
        }
        val newLength = Math.max(0, length - 3) // "..."
        return input.take(newLength) + "..."
    }

    private fun sgruntyScrive(chatId: Long, actionType: ActionType = ActionType.TYPING) {
        execute(
            SendChatAction().apply {
                this.setChatId(chatId)
                this.setAction(actionType)
            }
        )
        sleep(1..3)
    }

    private fun messaggio(textmd: String) {
        execute(
            SendMessage().apply {
                this.chatId = botConfig.chatId
                this.parseMode = ParseMode.HTML
                this.text = textmd
                this.disableWebPagePreview = true
            }
        )
    }

    private fun rispondiMessaggio(message: Message, textmd: String) {
        sgruntyScrive(message.chatId)
        execute(
            SendMessage().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.text = textmd
                this.disableWebPagePreview = true
            }
        )
    }

    private fun photo(caption: String, photo: InputFile) {
        execute(
            SendPhoto().apply {
                this.chatId = botConfig.chatId
                this.parseMode = ParseMode.HTML
                this.photo = photo
                this.caption = caption
            }
        )
    }

    private fun rispondiPhoto(message: Message, caption: String, photo: InputFile) {
        sgruntyScrive(message.chatId, ActionType.UPLOADDOCUMENT)
        execute(
            SendPhoto().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.photo = photo
                this.caption = caption
            }
        )
    }

    private fun audio(audio: InputFile) {
        execute(
            SendAudio().apply {
                this.chatId = botConfig.chatId
                this.audio = audio
            }
        )
    }

    private fun rispondiAudio(message: Message, audio: InputFile) {
        sgruntyScrive(message.chatId, ActionType.UPLOADDOCUMENT)
        execute(
            SendAudio().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.audio = audio
            }
        )
    }

    companion object {
        infix fun <T> T.elseIfNull(other: T?): T? {
            if (this != null) {
                return this
            }
            return other
        }

        fun String.urlEncode(): String {
            return URLEncoder.encode(this, StandardCharsets.UTF_8)
        }

        fun Iterable<*>?.length(): Long {
            return StreamSupport.stream(this?.spliterator(), false).count()
        }

        fun LocalDateTime.toDate(): Date {
            return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
        }

        fun JsonNode.dateTime(): LocalDateTime {
            return LocalDateTime.parse(this.textValue(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        }
    }
}
