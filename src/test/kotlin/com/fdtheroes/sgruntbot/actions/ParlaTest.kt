package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class ParlaTest : ActionTest() {

    private val parla = Parla()

    @Test
    fun testPositive() {
        parla.doAction(message("!parla questo bot è stupendo!"), Context())

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("Mi dicono di dire: questo bot è stupendo!")
    }

}