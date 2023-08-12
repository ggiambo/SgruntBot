package com.fdtheroes.sgruntbot.scheduled.random;

import com.fdtheroes.sgruntbot.scheduled.Scheduled
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextLong

interface ScheduledRandom : Scheduled {

    override fun firstRun() = randomTra16e36Ore()

    override fun nextRun() = randomTra16e36Ore()

    private fun randomTra16e36Ore(): LocalDateTime = LocalDateTime.now()
        .plusHours(nextLong(16, 36))
}
