package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Disabled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
@Disabled
class Bellissim(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex = Regex("bellissim", RegexOption.IGNORE_CASE)

    private val risposte = listOf(
        "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!",
        "IO sono bellissimo! .... vabbé, facciamo a turni.",
        "IO sono bellissimo! .... quasi bello come Giambo <3",
        "IO sono bellissimo! .... tu sei brutto come il culo di un cane da caccia",
        "IO sono bellissimo! .... quante volte devo ripetermi?",
    )

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message(risposte.random()), message)
        }
    }
}
