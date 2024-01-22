package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.YearProgress
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class ScheduledYearProgression(private val sgruntBot: Bot) : Scheduled {

    // random tra 5 e 10 giorni
    override fun firstRun(): LocalDateTime {
        val giorni = Random.nextLong(5, 10)
        return LocalDateTime.now().plusDays(giorni)
    }

    // random tra 10 e 20 giorni
    override fun nextRun(): LocalDateTime {
        val giorni = Random.nextLong(10, 20)
        return LocalDateTime.now().plusDays(giorni)
    }

    override fun execute() {
        val yearProgression = YearProgress.yearProgression()
        sgruntBot.messaggio(ActionResponse.message(yearProgression, false))
    }
}
