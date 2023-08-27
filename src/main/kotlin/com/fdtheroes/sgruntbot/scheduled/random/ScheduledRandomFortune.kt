package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.Fortune
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

//@Service
class ScheduledRandomFortune(private val fortune: Fortune, private val sgruntBot: Bot) : ScheduledRandom {
    override fun execute() {
        sgruntBot.messaggio(ActionResponse.message(fortune.getFortune(), false))
    }

}