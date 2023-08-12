package com.fdtheroes.sgruntbot.scheduled

import java.time.LocalDateTime

interface Scheduled {

    fun firstRun(): LocalDateTime

    fun nextRun(): LocalDateTime

    fun execute()
}