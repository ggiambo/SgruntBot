package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.persistence.KarmaService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class TestScheduledKarma : BaseTest() {

    private val karmaService = mock<KarmaService> {
        on { testoKarmaReport() } doReturn "karma Service ha fatto qualcosa"
    }
    private val scheduledKarma = ScheduledKarma(botUtils, karmaService)

    @Test
    fun scheduledKarmaTest() {
        scheduledKarma.execute()

        val argumentCaptor = argumentCaptor<ActionResponse, Boolean>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.first.capture(), any())
        val actionResponse = argumentCaptor.first.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(actionResponse.message).isEqualTo("karma Service ha fatto qualcosa")
        Assertions.assertThat(actionResponse.inputFile).isNull()
    }
}