package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ScheduledRandomCuloDiPapaTest : BaseTest() {

    private val randomCuloDiPapa = ScheduledRandomCuloDiPapa(botUtils)

    @Test
    fun testRandomCuloDiPapa() {
        randomCuloDiPapa.execute()

        val argumentCaptor = argumentCaptor<ActionResponse, Boolean>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.first.capture(), any())
        val actionResponse = argumentCaptor.first.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).isNotEmpty()
        assertThat(actionResponse.inputFile).isNull()
    }
}