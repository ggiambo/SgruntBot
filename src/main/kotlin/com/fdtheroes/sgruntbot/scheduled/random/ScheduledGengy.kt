package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.isToday
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextLong

//@Service
class ScheduledGengy(private val botUtils: BotUtils) : Scheduled {

    override fun firstRun() = traLeSeiELeSette()

    override fun nextRun() = traLeSeiELeSette()

    override fun execute() {
        val gengy = botUtils.getUserLink(botUtils.getChatMember(Users.F.id))
        val testo = "\uD83D\uDEAC $gengy, quante sigarette hai fumato ieri?"
        botUtils.messaggio(ActionResponse.message(testo))
    }

    private fun traLeSeiELeSette(): LocalDateTime {
        val traLeSeiELeSette = LocalDate.now()
            .atStartOfDay()
            .withHour(6)
            .plusMinutes(nextLong(0, 60))

        if (traLeSeiELeSette.isToday()) {
            return traLeSeiELeSette.plusDays(1)
        }
        return traLeSeiELeSette
    }
}