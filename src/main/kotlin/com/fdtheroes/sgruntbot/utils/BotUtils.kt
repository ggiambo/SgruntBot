package com.fdtheroes.sgruntbot.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import jakarta.annotation.PostConstruct
import okhttp3.Headers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.util.TelegramOkHttpClientFactory.HttpProxyOkHttpClientCreator
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.reactions.SetMessageReaction
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.chatmember.MemberStatus
import org.telegram.telegrambots.meta.api.objects.message.Message
import org.telegram.telegrambots.meta.api.objects.reactions.ReactionType
import org.telegram.telegrambots.meta.generics.TelegramClient
import java.io.InputStream
import java.net.Proxy
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.StreamSupport
import kotlin.math.max
import kotlin.random.Random

@Service
class BotUtils(private val botConfig: BotConfig, private val objectMapper: ObjectMapper) {

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
        params: List<Pair<String, String>> = emptyList(),
        body: Any? = null,
        headers: List<Pair<String, String>> = emptyList(),
        proxy: Proxy = botConfig.proxy,
    ): InputStream {
        val client = OkHttpClient().newBuilder()
            .proxy(proxy)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
        val request = if (body == null) {
            Request.Builder().get()
        } else {
            Request.Builder().post(objectMapper.writeValueAsBytes(body).toRequestBody())
        }
            .headers(
                Headers.Builder().apply {
                    headers.forEach { this.add(it.first, it.second) }
                }.build()
            )
            .url(
                url.toHttpUrl().newBuilder().apply {
                    params.forEach { this.addQueryParameter(it.first, it.second) }
                }.build()
            )
            .build()

        return client.newCall(request).execute().body!!.byteStream()
    }

    fun textFromURL(
        url: String,
        params: List<Pair<String, String>> = emptyList(),
        body: Any? = null,
        headers: List<Pair<String, String>> = emptyList(),
        proxy: Proxy = botConfig.proxy,
    ): String {
        return streamFromURL(url, params, body, headers, proxy)
            .readAllBytes()
            .decodeToString()
    }

    fun rispondi(actionMessage: ActionResponse, message: Message, disableWebPagePreview: Boolean = true) {
        when (actionMessage.type) {
            ActionResponseType.Message -> rispondiMessaggio(message, actionMessage.message, disableWebPagePreview)
            ActionResponseType.Photo -> rispondiPhoto(message, actionMessage.message, actionMessage.inputFile!!)
            ActionResponseType.Audio -> rispondiAudio(message, actionMessage.inputFile!!, actionMessage.thumbnail)
        }
    }

    fun messaggio(actionMessage: ActionResponse, disableWebPagePreview: Boolean = true) {
        when (actionMessage.type) {
            ActionResponseType.Message -> messaggio(actionMessage.message, disableWebPagePreview)
            ActionResponseType.Photo -> photo(actionMessage.message, actionMessage.inputFile!!)
            ActionResponseType.Audio -> audio(actionMessage.inputFile!!)
        }
    }

    fun reaction(message: Message, reactionType: ReactionType) {
        telegramClient.execute(
            SetMessageReaction(
                message.chatId.toString(),
                message.messageId,
                listOf(reactionType),
                true
            )
        )
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
        if (chatMember.status == MemberStatus.KICKED) {
            return null
        }
        if (chatMember.status == MemberStatus.LEFT) {
            return null
        }
        return chatMember.user
    }

    fun trimString(input: String, length: Int): String {
        if (input.length < length) {
            return input
        }
        val newLength = max(0, length - 3) // "..."
        return input.take(newLength) + "..."
    }

    fun sgruntyScrive(chatId: String, actionType: ActionType = ActionType.TYPING) {
        telegramClient.execute(
            SendChatAction(chatId, actionType.name)
        )
    }

    private fun messaggio(text: String, disableWebPagePreview: Boolean) {
        telegramClient.execute(
            SendMessage(botConfig.chatId, text).apply {
                this.parseMode = ParseMode.HTML
                this.disableWebPagePreview = disableWebPagePreview
            }
        )
    }

    private fun rispondiMessaggio(message: Message, text: String, disableWebPagePreview: Boolean) {
        sgruntyScrive(message.chatId.toString())
        telegramClient.execute(
            SendMessage(message.chatId.toString(), text).apply {
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.disableWebPagePreview = disableWebPagePreview
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

    private fun rispondiAudio(message: Message, audio: InputFile, thumbnail: InputFile? = null) {
        sgruntyScrive(message.chatId.toString(), ActionType.UPLOAD_VOICE)
        telegramClient.execute(
            SendAudio(message.chatId.toString(), audio).apply {
                this.replyToMessageId = message.messageId
                this.thumbnail = thumbnail
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

        fun Iterable<*>?.length(): Long {
            return StreamSupport.stream(this?.spliterator(), false).count()
        }

        fun LocalDateTime.toDate(): Date {
            return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
        }

        fun LocalDateTime.isToday(): Boolean {
            return this.toLocalDate() == LocalDateTime.now().toLocalDate()
        }

    }
}
