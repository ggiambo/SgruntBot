package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.persistence.StatsService
import com.fdtheroes.sgruntbot.utils.PieChartUtils
import com.fdtheroes.sgruntbot.utils.StatsUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TestScheduledStats : BaseTest() {

    private val statsService = mock<StatsService> {
        on { getStatsFromDate(any()) } doAnswer { args ->
            val startDate = args.arguments[0] as LocalDate
            val daysAgo = ChronoUnit.DAYS.between(startDate, LocalDate.now())
            (1..daysAgo).map {
                Stats(
                    userId = it,
                    statDay = LocalDate.now().minusDays(it),
                    messages = it.toInt() * 7
                )
            }
        }
    }
    private val scheduledStats = ScheduledStats(botUtils, StatsUtil(statsService, PieChartUtils(botUtils)))

    @Test
    fun scheduledStatsTest() {
        scheduledStats.execute()

        val argumentCaptor = argumentCaptor<ActionResponse, Boolean>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.first.capture(), any())
        val actionResponse = argumentCaptor.first.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(actionResponse.message).isEqualTo("")
        Assertions.assertThat(actionResponse.inputFile).isNotNull()
        Assertions.assertThat(actionResponse.inputFile!!.mediaName).isEqualTo("stats.jpg")
    }
}