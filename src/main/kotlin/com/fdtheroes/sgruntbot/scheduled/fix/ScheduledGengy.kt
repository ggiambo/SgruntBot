package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduledGengy(private val botUtils: BotUtils) : Scheduled {

    override fun firstRun() = noveDiSera()

    override fun nextRun() = noveDiSera()

    override fun execute() {
        val gengy = botUtils.getUserLink(botUtils.getChatMember(Users.F.id))
        val testo = "\uD83D\uDEAC $gengy, quante sigarette hai fumato oggi?"
        botUtils.messaggio(ActionResponse.message(testo))
    }

    private fun noveDiSera(): LocalDateTime {
        val noveDiSera = LocalDateTime.now()
            .withHour(21)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        if (noveDiSera.isBefore(LocalDateTime.now())) {
            return noveDiSera.plusDays(1)
        }
        return noveDiSera
    }
}