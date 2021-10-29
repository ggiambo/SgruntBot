package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class CheOreSonoTest : ActionTest() {

    private val cheOreSono = CheOreSono()

    @Test
    fun testPositive() {
        cheOreSono.doAction(message("non so che ore sono a dire il vero"))

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text)
            .contains("(precisamente ")
            .endsWith("ok?)")
    }
}