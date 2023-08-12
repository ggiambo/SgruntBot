package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.Fortune
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ScheduledRandomFortuneTest : BaseTest() {

    private val fortune = Fortune()
    private val randomFortune = ScheduledRandomFortune(fortune, sgruntBot,)

    @Test
    fun testRandomFortune() {
        randomFortune.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).isNotEmpty()
        assertThat(actionResponse.inputFile).isNull()
        assertThat(actionResponse.rispondi).isFalse()
    }
}