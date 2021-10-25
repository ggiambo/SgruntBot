package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class ChiEraTest : ActionTest() {

    private val chiEra = ChiEra()

    @Test
    fun testPositive() {
        chiEra.doAction(message("!chiera"), Context().apply { this.lastSuper = message("blah blah") })

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("[Pippo](tg://user?id=42)")
    }

    @Test
    fun testPositive_2() {
        chiEra.doAction(
            message("!chiera"),
            Context().apply {
                this.lastSuper = message("blah blah", from = user(42, userName = "", firstName = "Topopippo"))
            })

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("[Topopippo](tg://user?id=42)")
    }

    @Test
    fun testNegative() {
        chiEra.doAction(message("!chiera"), Context())

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEmpty()
    }
}