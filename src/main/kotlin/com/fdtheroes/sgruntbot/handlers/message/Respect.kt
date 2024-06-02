package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Respect(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    override fun handle(message: Message) {
        val don = message.replyToMessage?.from
        if (message.text == "F" && don != null) {
            botUtils.rispondi(ActionResponse.message("Baciamo le mani Don ${botUtils.getUserLink(don)}"), message)
        }
    }

    override fun halp() = "<b>F</b> baciamo le mani dell'autore!"
}
