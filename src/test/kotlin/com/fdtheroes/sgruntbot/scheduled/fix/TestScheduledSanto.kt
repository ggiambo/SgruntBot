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
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        Assertions.assertThat(actionResponse.type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(actionResponse.message).startsWith("<a href='https://www.santodelgiorno.it/")
        Assertions.assertThat(actionResponse.inputFile).isNotNull()
        Assertions.assertThat(actionResponse.rispondi).isFalse()
    }
}