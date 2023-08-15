package com.fdtheroes.sgruntbot.scheduled.fix

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class TestScheduledAMezzanotte {

    val scheduledAMezzanotte = ScheduledAMezzanotte { }

    @Test
    fun testFirstRun() {
        val firstRun = scheduledAMezzanotte.firstRun()
        val tomorrow = LocalDateTime.now().plusDays(1)
        Assertions.assertThat(firstRun).isEqualTo(
            LocalDateTime.of(
                tomorrow.year,
                tomorrow.month,
                tomorrow.dayOfMonth,
                0,
                0,
                0,
                0
            )
        )
    }

    @Test
    fun testNextRun() {
        val nextRun = scheduledAMezzanotte.nextRun()
        val tomorrow = LocalDateTime.now().plusDays(1)
        Assertions.assertThat(nextRun).isEqualTo(
            LocalDateTime.of(
                tomorrow.year,
                tomorrow.month,
                tomorrow.dayOfMonth,
                0,
                0,
                0,
                0
            )
        )
    }
}
