package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.scheduled.Scheduled
import java.time.LocalDateTime

interface ScheduledAMezzanotte : Scheduled {

    override fun firstRun() = getMezzanotte()

    override fun nextRun() = getMezzanotte()

    private fun getMezzanotte(): LocalDateTime {
        val mezzanotte = LocalDateTime.now()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        // mezzanotte è l'inizio di domani
        return mezzanotte.plusDays(1)
    }
}