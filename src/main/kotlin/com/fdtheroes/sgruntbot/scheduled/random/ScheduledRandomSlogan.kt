package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.handlers.message.Slogan
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils

//@Service
class ScheduledRandomSlogan(
    private val botUtils: BotUtils,
    private val botConfig: BotConfig,
    private val slogan: Slogan,
) : ScheduledRandom {

    override fun execute() {
        val lastAuthor = botConfig.lastAuthor
        val testo = if (lastAuthor == null) "" else slogan.fetchSlogan(lastAuthor)

        botUtils.messaggio(ActionResponse.message(testo, false))
    }

}
