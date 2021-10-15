package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.Bot
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime
import kotlin.random.Random

class Context {

    var lastSuper: Message? = null
    var lastAuthor : String? = null
    var pignolo = false
    var pausedTime: LocalDateTime? = null

    fun rispondiAsText(bot: Bot, message: Message, text: String) {
        sleep(0..5)
        val reply = SendMessage()
        reply.chatId = message.chatId.toString()
        reply.text = text
        reply.replyToMessageId = message.messageId
        bot.executeAsync(reply)
    }

    fun rispondi(bot: Bot, message: Message, textmd: String) {
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
        bot.executeAsync(reply)
    }

    fun rispondifile(bot: Bot, message: Message, textmd: String, document: Any) {
        // questo metodo c'era nell'originale in Ruby, ma non è mai stato usato
        // #suoraPasticciona
    }

    private fun sleep(seconds: IntRange) {
        Thread.sleep(Random.nextLong(seconds.first.toLong() * 1000, seconds.last.toLong() * 1000))
    }

}