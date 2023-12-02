package com.fdtheroes.sgruntbot.scheduled.random;

import com.fdtheroes.sgruntbot.scheduled.Scheduled
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextLong

fun interface ScheduledRandom : Scheduled {

    // random tra 6 e 10 ore
    override fun firstRun(): LocalDateTime {
        val minuti = nextLong(6 * 60, 10 * 60)
        return LocalDateTime.now().plusMinutes(minuti)
    }


    // random tra 10 e 14 ore
    override fun nextRun(): LocalDateTime {
        val minuti = nextLong(10 * 60, 14 * 60)
        return LocalDateTime.now().plusMinutes(minuti)
    }
}
