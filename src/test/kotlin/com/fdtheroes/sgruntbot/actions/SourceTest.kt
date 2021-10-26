package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class SourceTest : ActionTest() {

    private val source = Source()

    @Test
    fun testPositive() {
        source.doAction(message("!source"), Context())

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("http://github.com/ggiambo/SgruntBot")
    }

}