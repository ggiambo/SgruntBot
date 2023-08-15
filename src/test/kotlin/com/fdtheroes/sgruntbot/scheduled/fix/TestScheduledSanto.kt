package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class TestScheduledSanto : BaseTest() {

    private val scheduledSanto = ScheduledSanto(botUtils, mapper, sgruntBot)

    @Test
    fun scheduledSantoTest() {
        scheduledSanto.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(2)).messaggio(argumentCaptor.capture())
        val actionResponse1 = argumentCaptor.firstValue
        Assertions.assertThat(actionResponse1.type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(actionResponse1.message).startsWith("<a href='https://www.santodelgiorno.it/")
        Assertions.assertThat(actionResponse1.inputFile).isNotNull()
        Assertions.assertThat(actionResponse1.rispondi).isFalse()

        val actionResponse2 = argumentCaptor.secondValue
        Assertions.assertThat(actionResponse2.type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(actionResponse2.message).startsWith("<b>Altri santi</b>")
        Assertions.assertThat(actionResponse2.inputFile).isNull()
        Assertions.assertThat(actionResponse2.rispondi).isFalse()
    }
}
