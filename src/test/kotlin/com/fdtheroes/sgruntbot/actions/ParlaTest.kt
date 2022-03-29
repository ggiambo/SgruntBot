package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class ParlaTest : BaseTest() {

    private val parla = Parla(botConfig)

    @Test
    fun testPositive() {
        parla.doAction(message("!parla questo bot è stupendo!"), sgruntBot)

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("Mi dicono di dire: questo bot è stupendo!")
    }

}