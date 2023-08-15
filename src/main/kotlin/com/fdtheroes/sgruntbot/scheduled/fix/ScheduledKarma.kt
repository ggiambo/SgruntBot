package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.springframework.stereotype.Service

@Service
class ScheduledKarma(private val karmaService: KarmaService, private val sgruntBot: Bot) : ScheduledAMezzanotte {

    override fun execute() {
        val testo = karmaService.testoKarmaReport(sgruntBot)
        sgruntBot.messaggio(ActionResponse.message(testo, false))
    }
}
