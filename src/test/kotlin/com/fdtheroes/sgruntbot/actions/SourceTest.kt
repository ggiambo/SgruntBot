package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class SourceTest : BaseTest() {

    private val source = Source(sgruntBot)

    @Test
    fun testPositive() {
        source.doAction(message("!source"))

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("http://github.com/ggiambo/SgruntBot")
    }

}