package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

internal class ScommessinaTest : BaseTest() {

    val scommessina = Scommessina(botUtils, botConfig)

    @Test
    fun handle_scommessina() {
        scommessina.handle(message("!scommessina"))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Lancio una moneta, se viene testa vinco io, se viene croce perdi tu.")
    }
    @Test
    fun handle_scommessa() {
        scommessina.handle(message("!scommessa"))
        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Lancio una moneta, se viene testa vinco io, se viene croce perdi tu.")
    }
    @Test
    fun handle_scommessona() {
        scommessina.handle(message("!scommessona"))
        assertThat(actionResponses).hasSize(0)
    }

}