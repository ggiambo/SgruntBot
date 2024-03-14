package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.persistence.ErrePiGiService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class TestScheduledErrePiGi : BaseTest() {

    private val errePiGiService = mock<ErrePiGiService> {
        onGeneric { testoErrePiGiReport() } doReturn "ErrePiGi ha fattto qualcosa"
    }

    private val scheduledErrePiGi = ScheduledErrePiGi(botUtils, errePiGiService)

    @Test
    fun scheduledErrePiGiTest() {
        scheduledErrePiGi.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(actionResponse.message).isEqualTo("ErrePiGi ha fattto qualcosa")
        Assertions.assertThat(actionResponse.inputFile).isNull()
        Assertions.assertThat(actionResponse.rispondi).isFalse()
    }

}