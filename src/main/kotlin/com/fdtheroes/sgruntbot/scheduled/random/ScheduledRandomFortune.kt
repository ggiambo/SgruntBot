package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.handlers.message.Fortune
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils

//@Service
class ScheduledRandomFortune(private val botUtils: BotUtils, private val fortune: Fortune) : ScheduledRandom {
    override fun execute() {
        botUtils.messaggio(ActionResponse.message(fortune.getFortune(), false))
    }

}