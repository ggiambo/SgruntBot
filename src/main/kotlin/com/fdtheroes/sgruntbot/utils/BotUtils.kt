package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.Bot
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

class BotUtils {

    private lateinit var bot: Bot

    fun init(bot: Bot) {
        instance = BotUtils()
        instance.bot = bot
    }

    val userIds = Users.values().associateBy { it.id }

    val chatId = "-1001103213994"

    fun getUserLink(message: Message?): String {
        if (message == null) {
            return ""
        }
        val id = message.from.id
        val name: String
        if (message.from?.userName != null) {
            name = message.from.userName
        } else {
            name = message.from.firstName
        }
        return "[${name}](tg://user?id=${id})"
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
        bot.executeAsync(sendChatAction)
        sleep(3..5)
        val reply = SendMessage()
        reply.chatId = message.chat.id.toString()
        reply.replyToMessageId = message.messageId
        reply.parseMode = ParseMode.MARKDOWN
        reply.text = textmd
        rispondi(reply)
    }

    fun rispondi(sendMessage: SendMessage) {
        bot.executeAsync(sendMessage)
    }

    private fun sleep(seconds: IntRange) {
        Thread.sleep(Random.nextLong(seconds.first.toLong() * 1000, seconds.last.toLong() * 1000))
    }

    enum class Users(val id: Long) {
        SUORA(32657811L),
        GIAMBO(353708759L),
        DADA(252800958L),
        SEU(68714652L),
        GENGY(259607683L),
        AVVE(10427888L)
    }

    companion object {
        lateinit var instance: BotUtils
    }

}