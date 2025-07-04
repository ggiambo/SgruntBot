package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.time.LocalDateTime

class TestScheduledSanto : BaseTest() {

    private val scheduledSanto = ScheduledSanto(botUtils, mapper)

    @Test
    fun scheduledSantoTest() {
        scheduledSanto.execute()

        val argumentCaptor = argumentCaptor<ActionResponse, Boolean>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.first.capture(), any())
        val actionResponse1 = argumentCaptor.first.firstValue
        Assertions.assertThat(actionResponse1.type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(actionResponse1.message).startsWith("<a href='https://www.santodelgiorno.it/")
        Assertions.assertThat(actionResponse1.inputFile).isNotNull()
        /*
                val actionResponse2 = argumentCaptor.secondValue
                Assertions.assertThat(actionResponse2.type).isEqualTo(ActionResponseType.Message)
                Assertions.assertThat(actionResponse2.message).startsWith("<b>Altri santi</b>")
                Assertions.assertThat(actionResponse2.inputFile).isNull()
                Assertions.assertThat(actionResponse2.rispondi).isFalse()
         */
    }

    @Test
    fun testFirstRun() {
        val tomorrow = LocalDateTime.now().plusDays(1)
        val firstRun = scheduledSanto.firstRun()

        Assertions.assertThat(firstRun.year).isEqualTo(tomorrow.year)
        Assertions.assertThat(firstRun.month).isEqualTo(tomorrow.month)
        Assertions.assertThat(firstRun.dayOfMonth).isEqualTo(tomorrow.dayOfMonth)
        Assertions.assertThat(firstRun.hour).isEqualTo(6)
        Assertions.assertThat(firstRun.minute).isEqualTo(0)
        Assertions.assertThat(firstRun.second).isEqualTo(0)
        Assertions.assertThat(firstRun.nano).isEqualTo(0)
    }

    @Test
    fun testNextRun() {
        val tomorrow = LocalDateTime.now().plusDays(1)
        val nextRun = scheduledSanto.nextRun()

        Assertions.assertThat(nextRun.year).isEqualTo(tomorrow.year)
        Assertions.assertThat(nextRun.month).isEqualTo(tomorrow.month)
        Assertions.assertThat(nextRun.dayOfMonth).isEqualTo(tomorrow.dayOfMonth)
        Assertions.assertThat(nextRun.hour).isEqualTo(6)
        Assertions.assertThat(nextRun.minute).isEqualTo(0)
        Assertions.assertThat(nextRun.second).isEqualTo(0)
        Assertions.assertThat(nextRun.nano).isEqualTo(0)
    }
}
