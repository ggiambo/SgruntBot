package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

@Service
class Cazzate(private val sgruntBot: SgruntBot) : Action {

    private val cazzate = listOf("cazzate", "stronzate", "stupidate", "boiate figliolo")

    override fun doAction(message: Message) {
        if (nextInt(200) == 0) {
            val cosa = cazzate.random()
            sgruntBot.rispondi(message, "Ma la smetti di dire ${cosa}?")
        }
    }
}
