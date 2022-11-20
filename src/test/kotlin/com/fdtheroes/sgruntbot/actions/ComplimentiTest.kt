package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.persistence.ComplimentoService
import com.fdtheroes.sgruntbot.actions.persistence.Complimento
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class ComplimentiTest : BaseTest() {

    private val allComplimenti = listOf(
        Complimento(1, "Complimento 1"),
        Complimento(2, "Complimento 2"),
        Complimento(3, "Complimento 3"),
        Complimento(4, "Complimento 4"),
    )

    private val complimentoService = mock<ComplimentoService> {
        on { get(isA()) } doAnswer { answer ->
            allComplimenti.firstOrNull { it.userId == answer.arguments[0] }?.complimento
        }
    }

    private val complimenti = Complimenti(complimentoService)

    @Test
    fun testNegative() {
        complimenti.doAction(message("!quackQuack"), sgruntBot)

        assertThat(botArguments).isEmpty()
    }

    @Test
    fun testPositive_get() {
        complimenti.doAction(message("!complimento", user(1, "Utente uno")), sgruntBot)
        val argumentCaptor = argumentCaptor<Long>()

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("Ogni tanto ti dirò 'Complimento 1' <3")
        assertThat(sendMessage.text).isEqualTo("Ogni tanto ti dirò 'Complimento 1' <3")

        verify(complimentoService, times(1)).get(argumentCaptor.capture())
        assertThat(argumentCaptor.firstValue).isEqualTo(1)
        verify(complimentoService, times(0)).saveOrUpdate(isA(), isA())
    }

    @Test
    fun testPositive_update() {
        complimenti.doAction(message("!complimento Nuovo complimento", user(1, "Utente uno")), sgruntBot)
        val argumentCaptorUpdate1 = argumentCaptor<Long>()
        val argumentCaptorUpdate2 = argumentCaptor<String>()

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("Ogni tanto ti dirò 'Nuovo complimento' <3")

        verify(complimentoService, times(0)).get(isA())
        verify(complimentoService, times(1)).saveOrUpdate(argumentCaptorUpdate1.capture(), argumentCaptorUpdate2.capture())
        assertThat(argumentCaptorUpdate1.firstValue).isEqualTo(1)
        assertThat(argumentCaptorUpdate2.firstValue).isEqualTo("Nuovo complimento")
    }


}