package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Respect  : Action {

    override fun doAction(message: Message) {
        val don = message.replyToMessage?.from
        if (message.text == "F" && don != null) {
           BotUtils.rispondi(message.replyToMessage, "Baciamo le mani Don ${BotUtils.getUserLink(don)}")
        }
    }
}