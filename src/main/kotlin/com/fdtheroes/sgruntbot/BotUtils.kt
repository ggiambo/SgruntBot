package com.fdtheroes.sgruntbot

import org.slf4j.LoggerFactory
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.Serializable
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import kotlin.random.Random.Default.nextLong

object BotUtils {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private lateinit var bot: Bot
    private lateinit var proxy: Proxy

    const val chatId = "-1001103213994"

    fun init(bot: Bot) {
        init(bot, bot.options)
    }

    // used for testing
    fun init(bot: Bot, options: DefaultBotOptions) {
        this.bot = bot
        this.proxy = getProxy(options)
    }

    fun getUserName(user: User?): String {
        if (user == null) {
            return ""
        }
        val name: String
        if (!user.userName.isNullOrEmpty()) {
            name = user.userName
        } else {
            name = user.firstName
        }
        return name
    }

    fun getUserLink(user: User?): String {
        if (user == null) {
            return ""
        }
        val name = getUserName(user)
        return """<a href="tg://user?id=${user.id}">${name}</a>"""
    }

    fun rispondiAsText(message: Message, text: String) {
        sleep(0..5)
        val reply = SendMessage()
        reply.chatId = message.chatId.toString()
        reply.text = text
        reply.replyToMessageId = message.messageId
        rispondi(reply)
    }

    fun rispondi(message: Message, textmd: String) {
        val sendChatAction = SendChatAction()
        sendChatAction.chatId = message.chatId.toString()
        sendChatAction.setAction(ActionType.TYPING)
        rispondi(sendChatAction)
        sleep(3..5)
        val reply = SendMessage()
        reply.chatId = message.chat.id.toString()
        reply.replyToMessageId = message.messageId
        reply.parseMode = ParseMode.HTML
        reply.text = textmd
        rispondi(reply)
    }

    fun <T : Serializable, M : BotApiMethod<T>> rispondi(message: M): CompletableFuture<T>? {
        return bot.executeAsync(message)
    }

    fun rispondi(sendAudio: SendAudio): CompletableFuture<Message> {
        return bot.executeAsync(sendAudio)
    }

    fun rispondi(sendPhoto: SendPhoto): CompletableFuture<Message> {
        return bot.executeAsync(sendPhoto)
    }

    fun textFromURL(url: String, properties: Map<String, String> = emptyMap()): String {
        return URL(url)
            .openConnection(proxy).apply {
                properties.forEach {
                    setRequestProperty(it.key, it.value)
                }
            }
            .getInputStream()
            .readAllBytes()
            .decodeToString()
    }

    fun getChatMember(userId: Long): User? {
        val getChatMember = GetChatMember().apply {
            this.chatId = BotUtils.chatId
            this.userId = userId
        }
        return try {
            bot.execute(getChatMember)?.user
        } catch (e: Exception) {
            log.error("Problema con l'utente $userId", e)
            null
        }
    }

    private fun sleep(seconds: IntRange) {
        Thread.sleep(nextLong(seconds.first.toLong() * 1000, seconds.last.toLong() * 1000))
    }

    private fun getProxy(options: DefaultBotOptions): Proxy {
        if (options.proxyType == DefaultBotOptions.ProxyType.NO_PROXY) {
            return Proxy.NO_PROXY
        }
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(options.proxyHost, options.proxyPort))
    }

    fun String.urlEncode(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8)
    }
}
