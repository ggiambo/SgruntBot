package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class PorcaMadonnaTest : ActionTest() {

    private val porcaMadonna = PorcaMadonna()

    @Test
    fun testPositive() {
        Context.pignolo = true
        porcaMadonna.doAction(message(("\tporca madonna")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("...e tutti gli angeli in colonna!")
    }

    @Test
    fun testNegative() {
        porcaMadonna.doAction(message(("copporca madonna ")))

        assertThat(botArguments).isEmpty()
    }

}