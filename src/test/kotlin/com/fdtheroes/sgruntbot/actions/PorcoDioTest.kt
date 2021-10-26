package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class PorcoDioTest : ActionTest() {

    private val porcoDio = PorcoDio()

    @Test
    fun testPositive() {
        porcoDio.doAction(message(("\tporco dio")), Context().apply { pignolo = true })

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("E la madooonna!")
    }

    @Test
    fun testNegative() {
        porcoDio.doAction(message(("porco dioh! ")), Context())

        assertThat(botArguments).isEmpty()
    }

}