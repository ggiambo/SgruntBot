package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

class Cazzate : Action {

    private val cazzate = listOf("cazzate", "stronzate", "stupidate", "boiate figliolo")

    override fun doAction(message: Message) {
        if (nextInt(100) == 0) {
            val cosa = cazzate.random()
            BotUtils.rispondi(message, "Ma la smetti di dire ${cosa}?")
        }
    }
}