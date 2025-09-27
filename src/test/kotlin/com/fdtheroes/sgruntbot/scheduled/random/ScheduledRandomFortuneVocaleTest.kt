package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Fortune
import com.fdtheroes.sgruntbot.handlers.message.Vocale
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.objects.InputFile

class ScheduledRandomFortuneVocaleTest : BaseTest() {

    private val fortune = spy(Fortune(botUtils, botConfig)) {
        on { getFortune() } doReturn "Fake fortune, just for testing"
    }
    private val vocale = mock<Vocale> {
        on { getVocale(any()) } doReturn InputFile(
            "La Voce".byteInputStream(),
            "LaVoce.mp3"
        )
    }
    private val randomFortune = ScheduledRandomFortuneVocale(botUtils, fortune, vocale)

    @Test
    fun testRandomFortune() {
        randomFortune.execute()

        val argumentCaptor = argumentCaptor<ActionResponse, Boolean>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.first.capture(), any())
        val actionResponse = argumentCaptor.first.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Audio)
        assertThat(actionResponse.message).isNotEmpty()
        assertThat(actionResponse.inputFile).isNotNull()
        assertThat(actionResponse.inputFile!!.mediaName).isEqualTo("LaVoce.mp3")
        assertThat(actionResponse.inputFile!!.newMediaStream.readAllBytes().decodeToString()).isEqualTo("La Voce")
    }
}