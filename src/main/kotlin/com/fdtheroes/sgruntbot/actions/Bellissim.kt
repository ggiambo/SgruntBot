package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

class Bellissim : Action {

    private val regex = Regex("bellissim", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        if (regex.containsMatchIn(message.text)) {
            if (Random.nextBoolean()) {
                BotUtils.instance.rispondi(message, "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!")
            } else {
                BotUtils.instance.rispondi(message, "IO sono bellissimo! ....vabbé, facciamo a turni.")
            }
        }
    }
}