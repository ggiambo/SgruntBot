package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.handlers.message.HasHalp
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service

@Service
class ScheduledRandomPubblicita(
    private val botUtils: BotUtils,
    private val actions: List<HasHalp>,
) : ScheduledRandom {
    override fun execute() {
        val randomHalp = actions.random().halp()
        val messaggio = "Prova questa ficiur di Sgrunty!\n"
        botUtils.messaggio(ActionResponse.message("$messaggio$randomHalp", false))
    }

}