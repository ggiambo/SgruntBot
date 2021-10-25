package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class BellissimTest : ActionTest() {

    private val bellissim = Bellissim()

    @Test
    fun testPositive() {
        bellissim.doAction(message(("XYZ_bEllISSimo_123")), Context())

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("IO sono bellissimo! ....")
    }

    @Test
    fun testNegative() {
        bellissim.doAction(message(("XYZ_quack_123")), Context())

        assertThat(botArguments).hasSize(0)
    }
}