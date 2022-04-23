package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextBoolean

@Service
class Bellissim : Action {

    private val regex = Regex("bellissim", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            if (nextBoolean()) {
                sgruntBot.rispondi(message, "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!")
            } else {
                sgruntBot.rispondi(message, "IO sono bellissimo! ....vabbé, facciamo a turni.")
            }
        }
    }
}
