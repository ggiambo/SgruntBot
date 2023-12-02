package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiService

// @Service
class ScheduledErrePiGi(
    private val errePiGiService: ErrePiGiService,
    private val sgruntBot: Bot
) : ScheduledAMezzanotte {

    override fun execute() {
        val testo = errePiGiService.testoErrePiGiReport(sgruntBot::getChatMember)
        if (testo != null) {
            sgruntBot.messaggio(ActionResponse.message(testo, false))
        }
        errePiGiService.reset()
    }
}
