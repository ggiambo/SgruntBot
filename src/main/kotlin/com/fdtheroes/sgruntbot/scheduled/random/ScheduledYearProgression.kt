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

    override fun firstRun() = getNext()

    override fun nextRun() = getNext()

    // random tra 5 e 10 giorni
    private fun getNext(): LocalDateTime {
        val giorni = Random.nextLong(5, 10)
        return LocalDateTime.now().plusDays(giorni)
    }

    override fun execute() {
        val yearProgression = YearProgress.yearProgression()
        sgruntBot.messaggio(ActionResponse.message(yearProgression, false))
    }
}
