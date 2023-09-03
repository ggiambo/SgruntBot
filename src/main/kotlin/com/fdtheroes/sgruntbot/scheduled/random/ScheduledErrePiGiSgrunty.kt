package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiService
import org.springframework.stereotype.Service

@Service
class ScheduledErrePiGiSgrunty(
    private val errePiGiService: ErrePiGiService,
    private val sgruntBot: Bot,
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
        val testoAttacco = errePiGiService.sgruntyAttacca(sgruntBot::getChatMember)

        if (testoAttacco != null) {
            val testo = "Sgrunty ${ragioni.random()}\n$testoAttacco"
            sgruntBot.messaggio(ActionResponse.message(testo))
        }
    }
}