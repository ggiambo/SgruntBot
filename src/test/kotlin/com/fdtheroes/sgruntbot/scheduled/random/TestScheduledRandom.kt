package com.fdtheroes.sgruntbot.scheduled.random

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TestScheduledRandom {

    private val scheduledRandom = ScheduledRandom { }

    @Test
    fun testFirstRun() {
        val firstRun = scheduledRandom.firstRun()

        assertThat(firstRun).isAfter(LocalDateTime.now().plusHours(6))
        assertThat(firstRun).isBefore(LocalDateTime.now().plusHours(10))
    }

    @Test
    fun testNextRun() {
        val firstRun = scheduledRandom.nextRun()

        assertThat(firstRun).isAfter(LocalDateTime.now().plusHours(10))
        assertThat(firstRun).isBefore(LocalDateTime.now().plusHours(14))
    }

}
