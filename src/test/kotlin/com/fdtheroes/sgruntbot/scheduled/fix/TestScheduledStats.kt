package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class TestScheduledStats : BaseTest() {

    private val statsService = mock<StatsService> {

    }
    private val scheduledStats = ScheduledStats(sgruntBot, botUtils, statsService)

    @Test
    fun scheduledStatsTest() {
        scheduledStats.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(actionResponse.message).isEqualTo("")
        Assertions.assertThat(actionResponse.inputFile).isNotNull()
        Assertions.assertThat(actionResponse.inputFile!!.mediaName).isEqualTo("stats.jpg")
        Assertions.assertThat(actionResponse.rispondi).isFalse()
    }
}