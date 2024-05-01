package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.handlers.message.YearProgress
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ScheduledYearProgression(private val botUtils: BotUtils) : Scheduled {

    override fun firstRun() = getNext()

    override fun nextRun() = getNext()

    // allo scattare della percentuale
    private fun getNext(): LocalDateTime {
        val onePercentInSeconds = (LocalDate.now().lengthOfYear() * 24 * 60 * 60) / 100.toLong()
        var next = LocalDateTime.now()
            .withMonth(1)
            .withDayOfMonth(1)
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        while (next.isBefore(LocalDateTime.now())) {
            next = next.plusSeconds(onePercentInSeconds)
        }

        return next
    }

    override fun execute() {
        val yearProgression = YearProgress.yearProgression()
        botUtils.messaggio(ActionResponse.message(yearProgression))
    }
}
