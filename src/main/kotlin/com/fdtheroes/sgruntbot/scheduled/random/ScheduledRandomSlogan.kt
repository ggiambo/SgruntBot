package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.Slogan
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

//@Service
class ScheduledRandomSlogan(
    private val slogan: Slogan,
    private val sgruntBot: Bot,
    private val botConfig: BotConfig,
) : ScheduledRandom {

    override fun execute() {
        val lastAuthor = botConfig.lastAuthor
        val testo = if (lastAuthor == null) "" else slogan.fetchSlogan(lastAuthor)

        sgruntBot.messaggio(ActionResponse.message(testo, false))
    }

}
