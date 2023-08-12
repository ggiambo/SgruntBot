package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ScheduledRandomCuloDiPapaTest : BaseTest() {

    private val randomCuloDiPapa = ScheduledRandomCuloDiPapa(botUtils, sgruntBot)

    @Test
    fun testRandomCuloDiPapa() {
        randomCuloDiPapa.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).isNotEmpty()
        assertThat(actionResponse.inputFile).isNull()
        assertThat(actionResponse.rispondi).isFalse()
    }
}