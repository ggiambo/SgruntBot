package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.persistence.StatsRepository
import com.fdtheroes.sgruntbot.persistence.StatsService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File
import java.time.LocalDate

class StatsUtilTest : BaseTest() {

    private val statsService = StatsService(
        mock<StatsRepository> {
            on { findStatsByStatDayBetween(any(), any()) } doReturn stats()
        }
    )

    private val statsUtil = StatsUtil(statsService, botUtils)

    @Test
    fun getStats() {
        val stats = statsUtil.getStats(42)
        val statsOut = this.javaClass.getResourceAsStream("/statsOut.png")

        Assertions.assertThat(statsOut.readAllBytes()).isEqualTo(stats.newMediaStream.readAllBytes())
    }

    private fun stats(): List<Stats> {
        return (1..5).map {
            Stats(
                it.toLong(),
                LocalDate.now(),
                it * 10
            )
        }
    }
}