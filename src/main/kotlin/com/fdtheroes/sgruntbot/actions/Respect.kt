package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Respect(
    private val sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
) : Action, HasHalp {

    override fun doAction(message: Message) {
        val don = message.replyToMessage?.from
        if (message.text == "F" && don != null) {
            sgruntBot.rispondi(message.replyToMessage, "Baciamo le mani Don ${botUtils.getUserLink(don)}")
        }
    }

    override fun halp() = "<b>F</b> baciamo le mani dell'autore!"
}
