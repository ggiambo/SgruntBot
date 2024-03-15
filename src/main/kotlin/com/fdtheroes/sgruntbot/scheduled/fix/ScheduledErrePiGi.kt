package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.ErrePiGiService
import com.fdtheroes.sgruntbot.utils.BotUtils

// @Service
class ScheduledErrePiGi(
    private val botUtils: BotUtils,
    private val errePiGiService: ErrePiGiService,
) : ScheduledAMezzanotte {

    override fun execute() {
        val testo = errePiGiService.testoErrePiGiReport()
        if (testo != null) {
            botUtils.messaggio(ActionResponse.message(testo, false))
        }
        errePiGiService.reset()
    }
}
