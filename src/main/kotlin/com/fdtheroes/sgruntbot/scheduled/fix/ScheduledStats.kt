package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.StatsUtil
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

@Service
class ScheduledStats(
    private val botUtils: BotUtils,
    private val statsUtil: StatsUtil,
) : Scheduled {
    override fun firstRun() = mezzanotteDomenicaProssima()

    override fun nextRun() = mezzanotteDomenicaProssima()

    override fun execute() {
        val inputFile = statsUtil.getStats(7)
        val actionResponse = ActionResponse.photo("", inputFile)
        botUtils.messaggio(actionResponse)
    }

    private fun mezzanotteDomenicaProssima(): LocalDateTime {
        val mezzanotte = LocalDateTime.now().withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
        return mezzanotte.with(TemporalAdjusters.next(DayOfWeek.MONDAY)) // Mezzanotte tra domenica e lunedi
    }

}
