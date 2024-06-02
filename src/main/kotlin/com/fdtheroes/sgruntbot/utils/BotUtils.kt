package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import jakarta.annotation.PostConstruct
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory.HttpProxyOkHttpClientCreator
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.io.InputStream
import java.net.Proxy
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.stream.StreamSupport
import kotlin.random.Random

@Service
class BotUtils(private val botConfig: BotConfig) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private lateinit var telegramClient: TelegramClient

    @PostConstruct
    fun postConstruct() {
        val okClientHttp = HttpProxyOkHttpClientCreator({ botConfig.proxy }, { null }).get()
        telegramClient = OkHttpTelegramClient(okClientHttp, botConfig.telegramToken)
    }

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
        params: List<String>? = null,
        headers: List<Pair<String, String>> = emptyList(),
        proxy: Proxy = botConfig.proxy,
    ): InputStream {
        val client = OkHttpClient().newBuilder()
            .proxy(proxy)
            .build()
        val request = Request.Builder()
            .url(String.format(url, *params.orEmpty().toTypedArray()))
            .apply { headers.forEach { header(it.first, it.second) } }
            .get()
            .build()

        return client.newCall(request).execute().body!!.byteStream()
    }

    fun textFromURL(
        url: String,
        params: List<String>? = null,
        headers: List<Pair<String, String>> = emptyList(),
        proxy: Proxy = botConfig.proxy,
    ): String {
        return streamFromURL(url, params, headers, proxy)
            .readAllBytes()
            .decodeToString()
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
        val getChatMember = GetChatMember(botConfig.chatId, userId)
        val chatMember = try {
            telegramClient.execute(getChatMember)
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

    private fun sgruntyScrive(chatId: String, actionType: ActionType = ActionType.TYPING) {
        telegramClient.execute(
            SendChatAction(chatId, actionType.name)
        )
        sleep(1..3)
    }

    private fun messaggio(text: String) {
        telegramClient.execute(
            SendMessage(botConfig.chatId, text).apply {
                this.parseMode = ParseMode.HTML
                this.disableWebPagePreview = true
            }
        )
    }

    private fun rispondiMessaggio(message: Message, text: String) {
        sgruntyScrive(message.chatId.toString())
        telegramClient.execute(
            SendMessage(message.chatId.toString(), text).apply {
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.disableWebPagePreview = true
            }
        )
    }

    private fun photo(caption: String, photo: InputFile) {
        telegramClient.execute(
            SendPhoto(botConfig.chatId, photo).apply {
                this.parseMode = ParseMode.HTML
                this.caption = caption
            }
        )
    }

    private fun rispondiPhoto(message: Message, caption: String, photo: InputFile) {
        sgruntyScrive(message.chatId.toString(), ActionType.UPLOAD_PHOTO)
        telegramClient.execute(
            SendPhoto(message.chatId.toString(), photo).apply {
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.caption = caption
            }
        )
    }

    private fun audio(audio: InputFile) {
        telegramClient.execute(
            SendAudio(botConfig.chatId, audio)
        )
    }

    private fun rispondiAudio(message: Message, audio: InputFile) {
        sgruntyScrive(message.chatId.toString(), ActionType.UPLOAD_VOICE)
        telegramClient.execute(
            SendAudio(message.chatId.toString(), audio).apply {
                this.replyToMessageId = message.messageId
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

    }
}
