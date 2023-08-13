package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class TestScheduledKarma : BaseTest() {

    private val karmaService = mock<KarmaService> {
        on { testoKarmaReport(isA()) } doReturn "karma Service ha fatto qualcosa"
    }
    private val scheduledKarma = ScheduledKarma(karmaService, sgruntBot)

    @Test
    fun scheduledKarmaTest() {
        scheduledKarma.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(actionResponse.message).isEqualTo("karma Service ha fatto qualcosa")
        Assertions.assertThat(actionResponse.inputFile).isNull()
        Assertions.assertThat(actionResponse.rispondi).isFalse()
    }
}