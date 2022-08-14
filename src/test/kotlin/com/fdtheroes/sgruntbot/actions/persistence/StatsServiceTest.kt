package com.fdtheroes.sgruntbot.actions.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

internal class StatsServiceTest {

    @Test
    fun increaseStats_notExisting() {
        val statsRepository: StatsRepository = mock {
            on { findStatsByUserIdAndStatDay(isA(), isA()) } doReturn null
        }
        val statsService = StatsService(statsRepository)
        val argumentCaptor = argumentCaptor<Stats>()

        statsService.increaseStats(42)

        verify(statsRepository, times(1)).save(argumentCaptor.capture())
        val savedStat = argumentCaptor.firstValue
        assertThat(savedStat.id).isNull()
        assertThat(savedStat.userId).isEqualTo(42)
        assertThat(savedStat.statDay).isEqualTo(LocalDate.now())
        assertThat(savedStat.messages).isEqualTo(1)
    }

    @Test
    fun increaseStats() {
        val statsRepository: StatsRepository = mock {
            on { findStatsByUserIdAndStatDay(isA(), isA()) } doReturn Stats(
                id = 12,
                userId = 42,
                messages = 100,
            )
        }
        val statsService = StatsService(statsRepository)
        val argumentCaptor = argumentCaptor<Stats>()

        statsService.increaseStats(42)

        verify(statsRepository, times(1)).save(argumentCaptor.capture())
        val savedStat = argumentCaptor.firstValue
        assertThat(savedStat.id).isEqualTo(12)
        assertThat(savedStat.userId).isEqualTo(42)
        assertThat(savedStat.statDay).isEqualTo(LocalDate.now())
        assertThat(savedStat.messages).isEqualTo(101)
    }

    @Test
    fun getStatsToday() {
        val statsRepository: StatsRepository = mock {
            on { findStatsByStatDayBetween(isA(), isA()) } doReturn listOf(
                Stats(
                    id = 1,
                    userId = 100,
                    messages = 3,
                ),
                Stats(
                    id = 2,
                    userId = 200,
                    messages = 5,
                ),
                Stats(
                    id = 3,
                    userId = 300,
                    messages = 7,
                ),
                Stats(
                    id = 4,
                    userId = 400,
                    messages = 9,
                )
            )
        }
        val statsService = StatsService(statsRepository)
        val argumentCaptor = argumentCaptor<LocalDate>()

        val stats = statsService.getStatsToday()

        assertThat(stats.size).isEqualTo(4)
        assertThat(stats.map { it.id }).containsOnly(1, 2, 3, 4)
        assertThat(stats.map { it.userId }).containsOnly(100, 200, 300, 400)
        assertThat(stats.map { it.statDay }).containsOnly(LocalDate.now())
        assertThat(stats.map { it.messages }).containsOnly(3, 5, 7, 9)
        verify(statsRepository, times(1)).findStatsByStatDayBetween(argumentCaptor.capture(), argumentCaptor.capture())
        val startStatDay = argumentCaptor.firstValue
        val endStatDay = argumentCaptor.secondValue
        assertThat(startStatDay).isEqualTo(LocalDate.now())
        assertThat(endStatDay).isEqualTo(LocalDate.now())

    }

    @Test
    fun getStatsThisMonthByDay() {
        val statDate = LocalDate.of(2022, 10, 25)
        val statsRepository: StatsRepository = mock {
            on { findStatsByStatDayBetween(isA(), isA()) } doReturn listOf(
                Stats(
                    id = 1,
                    userId = 100,
                    statDay = statDate.minusDays(1),
                    messages = 3,
                ),
                Stats(
                    id = 2,
                    userId = 100,
                    statDay = statDate.minusDays(2),
                    messages = 5,
                ),
                Stats(
                    id = 3,
                    userId = 200,
                    statDay = statDate.plusDays(1),
                    messages = 7,
                ),
                Stats(
                    id = 4,
                    userId = 200,
                    statDay = statDate.plusDays(2),
                    messages = 9,
                )
            )
        }
        val statsService = StatsService(statsRepository)

        val stats = statsService.getStatsThisMonthByDay()

        assertThat(stats.size).isEqualTo(4)
        assertThat(stats.keys).containsOnly(
            statDate.minusDays(2).dayOfMonth,
            statDate.minusDays(1).dayOfMonth,
            statDate.plusDays(1).dayOfMonth,
            statDate.plusDays(2).dayOfMonth,
        )
        val values = stats.values.flatten()
        assertThat(values.map { it.id }).containsOnly(1, 2, 3, 4)
        assertThat(values.map { it.userId }).containsOnly(100, 200)
        assertThat(values.map { it.statDay }).containsOnly(
            statDate.minusDays(2),
            statDate.minusDays(1),
            statDate.plusDays(1),
            statDate.plusDays(2),
        )
        assertThat(values.map { it.messages }).containsOnly(3, 5, 7, 9)
    }

    @Test
    fun getStatsThisMonth() {
        val statDate = LocalDate.of(2022, 10, 25)
        val statsRepository: StatsRepository = mock {
            on { findStatsByStatDayBetween(isA(), isA()) } doReturn listOf(
                Stats(
                    id = 1,
                    userId = 100,
                    statDay = statDate.minusDays(1),
                    messages = 3,
                ),
                Stats(
                    id = 2,
                    userId = 100,
                    statDay = statDate.minusDays(2),
                    messages = 5,
                ),
                Stats(
                    id = 3,
                    userId = 200,
                    statDay = statDate.plusDays(1),
                    messages = 7,
                ),
                Stats(
                    id = 4,
                    userId = 200,
                    statDay = statDate.plusDays(2),
                    messages = 9,
                )
            )
        }
        val statsService = StatsService(statsRepository)

        val stats = statsService.getStatsThisMonth()

        assertThat(stats.size).isEqualTo(2)
        assertThat(stats.map { it.id }).containsOnly(null)
        assertThat(stats.map { it.userId }).containsOnly(100, 200)
        assertThat(stats.map { it.statDay }).containsOnly(LocalDate.now())
        assertThat(stats.map { it.messages }).containsOnly(8, 16)
    }

}