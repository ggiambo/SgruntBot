package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

@Service
class ScheduledGengy(private val botUtils: BotUtils) : Scheduled {

    private val endDate = LocalDate.of(2025, Month.MARCH, 13).atStartOfDay()

    override fun firstRun() = noveDiSera()

    override fun nextRun() = noveDiSera()

    override fun execute() {
        val days = Duration.between(LocalDate.now().atStartOfDay(), endDate).toDays()
        if (days < 0) {
            return
        }
        val gengy = botUtils.getUserLink(botUtils.getChatMember(Users.F.id))
        val testo = "\uD83D\uDEAC Mancano $days giorni: $gengy, quante sigarette hai fumato oggi?"
        botUtils.messaggio(ActionResponse.message(testo))
    }

    private fun noveDiSera(): LocalDateTime {
        val noveDiSera = LocalDate.now()
            .atStartOfDay()
            .withHour(21)

        if (noveDiSera.isBefore(LocalDateTime.now())) {
            return noveDiSera.plusDays(1)
        }
        return noveDiSera
    }
}