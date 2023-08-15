package com.fdtheroes.sgruntbot.scheduled.random;

import com.fdtheroes.sgruntbot.scheduled.Scheduled
import java.time.LocalDateTime
import kotlin.random.Random

fun interface ScheduledRandom : Scheduled {

    // random tra 6 e 10 ore
    override fun firstRun() = LocalDateTime.now().plusHours(Random.nextLong(6, 10))

    // random trha 10 e 14 ore
    override fun nextRun() = LocalDateTime.now().plusHours(Random.nextLong(10, 14))
}
