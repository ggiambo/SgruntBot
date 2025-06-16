package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.persistence.StatsService
import com.fdtheroes.sgruntbot.utils.StatsUtil
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class TestScheduledStats : BaseTest() {

    private val statsService = mock<StatsService> {

    }
    private val scheduledStats = ScheduledStats(botUtils, StatsUtil(statsService, botUtils))

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