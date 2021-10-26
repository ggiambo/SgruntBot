package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class SloganTest : ActionTest() {

    private val slogan = Slogan()

    @Test
    fun testPositive() {
        slogan.doAction(message("!slogan la cacca molle"), Context())

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).containsIgnoringCase("la cacca molle")
    }

}