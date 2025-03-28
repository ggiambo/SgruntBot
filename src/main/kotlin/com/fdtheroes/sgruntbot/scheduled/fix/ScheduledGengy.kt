package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ScheduledGengy(private val botUtils: BotUtils) : Scheduled {

    override fun firstRun() = noveDiSera()

    override fun nextRun() = noveDiSera()

    override fun execute() {
        val gengy = botUtils.getUserLink(botUtils.getChatMember(Users.F.id))
        val testo = "\uD83D\uDEAC $gengy, quante sigarette hai fumato <b>nelle ultime 24 ore</b>?"
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