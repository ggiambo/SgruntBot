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
        Context.lastSuper = user()
        chiEra.doAction(message("!chiera"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("[Pippo](tg://user?id=42)")
    }

    @Test
    fun testPositive_2() {
        Context.lastSuper = user(42, userName = "", firstName = "Topopippo")
        chiEra.doAction(
            message("!chiera")
        )

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("[Topopippo](tg://user?id=42)")
    }

    @Test
    fun testNegative() {
        Context.lastSuper = null
        chiEra.doAction(message("!chiera"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEmpty()
    }
}