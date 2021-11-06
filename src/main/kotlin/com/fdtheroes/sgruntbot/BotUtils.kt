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
import org.telegram.telegrambots.meta.api.objects.chatmember.*
import java.io.Serializable
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import kotlin.random.Random.Default.nextLong

object BotUtils {

    private lateinit var bot: Bot
    private lateinit var proxy: Proxy

    private val log = LoggerFactory.getLogger(this.javaClass)

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
        log.info("user: $user")
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
        return "[${name}](tg://user?id=${user.id})"
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
        reply.parseMode = ParseMode.MARKDOWNV2
        reply.text = textmd
        rispondi(reply)
    }

    fun <T : Serializable, M : BotApiMethod<T>> rispondi(message: M) {
        bot.executeAsync(message)
    }

    fun rispondi(sendAudio: SendAudio) {
        bot.executeAsync(sendAudio)
    }

    fun rispondi(sendPhoto: SendPhoto) {
        bot.executeAsync(sendPhoto)
    }

    fun textFromURL(url: String): String {
        return URL(url)
            .openConnection(proxy)
            .getInputStream()
            .readAllBytes()
            .decodeToString()
    }

    fun getChatMember(userId: Long): User? {
        val getChatMember = GetChatMember().apply {
            this.chatId = BotUtils.chatId
            this.userId = userId
        }
        val chatMember = bot.execute(getChatMember)
        log.info("chatmember: $chatMember")
        return when (chatMember) {
            is ChatMemberAdministrator -> chatMember.user
            is ChatMemberBanned -> chatMember.user
            is ChatMemberLeft -> chatMember.user
            is ChatMemberMember -> chatMember.user
            is ChatMemberOwner -> chatMember.user
            is ChatMemberRestricted -> chatMember.user
            else -> null
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
}
