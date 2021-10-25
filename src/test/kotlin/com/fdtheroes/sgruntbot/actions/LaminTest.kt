package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class LaminTest : ActionTest() {

    private val lamin = Lamin()

    @Test
    fun testPositive() {
        lamin.doAction(message("negraccio"), Context())

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("Lamin mi manchi.")
    }

    @Test
    fun testPositive_2() {
        lamin.doAction(message("__negher++"), Context())

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("Lamin mi manchi.")
    }

    @Test
    fun testNegative() {
        lamin.doAction(message("**negrini!!"), Context())

        Assertions.assertThat(botArguments).isEmpty()
    }

}