package com.fdtheroes.sgruntbot.scheduled

import java.time.LocalDateTime

interface Scheduled {

    fun firstRun(): LocalDateTime

    fun nextRun(): LocalDateTime

    fun execute()

    fun oggiAlle(ore: Int): LocalDateTime {
        return LocalDateTime.now()
            .withHour(ore)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
    }
}