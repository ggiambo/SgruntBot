package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

class Last : Action {

    private val regex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.matches(message.text) && Context.lastAuthor != null) {
            val sendMessage = SendMessage()
            sendMessage.chatId = BotUtils.chatId
            sendMessage.parseMode = ParseMode.HTML
            sendMessage.text = Slogan.fetchSlogan(Context.lastAuthor!!)
            BotUtils.rispondi(sendMessage)
        }
    }

}