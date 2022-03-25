package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Respect  : Action, HasHalp {

    override fun doAction(message: Message) {
        val don = message.replyToMessage?.from
        if (message.text == "F" && don != null) {
           BotUtils.rispondi(message.replyToMessage, "Baciamo le mani Don ${BotUtils.getUserLink(don)}")
        }
    }

    override fun halp() = "<b>F</b> baciamo le mani dell'autore!"
}
