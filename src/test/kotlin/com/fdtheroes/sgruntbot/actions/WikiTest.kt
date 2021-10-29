package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class WikiTest : ActionTest() {

    private val wiki = Wiki()

    @Test
    fun testPositive() {
        wiki.doAction(message("!wiki giambo"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text)
            .startsWith("Il giambo (in greco antico: ")
            .endsWith("https://it.wikipedia.org/wiki/Giambo")
    }

}