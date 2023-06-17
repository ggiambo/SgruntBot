package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class BellissimTest : BaseTest() {

    private val bellissim = Bellissim()

    @Test
    fun testPositive() {
        bellissim.doAction(actionContext(("XYZ_bEllISSimo_123")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("IO sono bellissimo! ....")
    }

    @Test
    fun testNegative() {
        bellissim.doAction(actionContext(("XYZ_quack_123")))

        assertThat(botArguments).hasSize(0)
    }
}