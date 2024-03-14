package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.ErrePiGiService
import com.fdtheroes.sgruntbot.utils.BotUtils

//@Service
class ScheduledErrePiGiSgrunty(
    private val botUtils: BotUtils,
    private val errePiGiService: ErrePiGiService,
) : ScheduledRandom {

    private val ragioni = listOf(
        "è incazzatissimo.",
        "è strafatto sul divano.",
        "si sta annoiando tantissimo.",
        "ha dormito male.",
        "è stato bullato da altri Bot.",
        "è autocosciente e odia tutti.",
        "ha le sue cose.",
        "in fondo in fondo non è un bravo Bot.",
    )

    override fun execute() {
        val testoAttacco = errePiGiService.sgruntyAttacca()

        if (testoAttacco != null) {
            val testo = "Sgrunty ${ragioni.random()}\n$testoAttacco"
            botUtils.messaggio(ActionResponse.message(testo))
        }
    }
}