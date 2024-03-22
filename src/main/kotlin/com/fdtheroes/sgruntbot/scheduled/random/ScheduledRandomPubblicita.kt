package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.handlers.message.HasHalp
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class ScheduledRandomPubblicita(private val botUtils: BotUtils, private val actions: List<HasHalp>) : Scheduled {
    override fun execute() {
        val randomHalp = actions.random().halp()
        val messaggio = "Prova questa ficiur di Sgrunty!\n"
        botUtils.messaggio(ActionResponse.message("$messaggio$randomHalp", false))
    }

    // random tra 12 e 36 ore
    override fun firstRun(): LocalDateTime {
        val ore = Random.nextLong(12, 36)
        return LocalDateTime.now().plusHours(ore)
    }

    // random tra 12 e 24 ore
    override fun nextRun() = firstRun()

}
