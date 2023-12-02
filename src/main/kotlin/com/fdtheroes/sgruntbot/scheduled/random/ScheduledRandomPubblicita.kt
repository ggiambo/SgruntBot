package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.HasHalp
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class ScheduledRandomPubblicita(
    private val actions: List<HasHalp>,
    private val sgruntBot: Bot,
) : ScheduledRandom {
    override fun execute() {
        val randomHalp = actions.random().halp()
        val messaggio = "Prova questa ficiur di Sgrunty!\n"
        sgruntBot.messaggio(ActionResponse.message("$messaggio$randomHalp", false))
    }

}