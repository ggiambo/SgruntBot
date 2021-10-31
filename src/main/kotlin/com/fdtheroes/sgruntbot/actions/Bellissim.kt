package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextBoolean

class Bellissim : Action {

    private val regex = Regex("bellissim", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            if (nextBoolean()) {
                BotUtils.rispondi(message, "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!")
            } else {
                BotUtils.rispondi(message, "IO sono bellissimo! ....vabbé, facciamo a turni.")
            }
        }
    }
}