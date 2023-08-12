package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class TestScheduledErrePiGi : BaseTest() {

    private val errePiGiService = mock<ErrePiGiService> {
        onGeneric { testoErrePiGiReport(isA()) } doReturn "ErrePiGi ha fattto qualcosa"
    }

    private val scheduledErrePiGi = ScheduledErrePiGi(errePiGiService, sgruntBot)

    @Test
    fun scheduledErrePiGiTest() {
        scheduledErrePiGi.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(actionResponse.message).isEqualTo("ErrePiGi ha fattto qualcosa")
        Assertions.assertThat(actionResponse.inputFile).isNull()
        Assertions.assertThat(actionResponse.rispondi).isFalse()
    }

}