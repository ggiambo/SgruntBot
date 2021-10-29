package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class BullshitTest : ActionTest() {

    private val bullshit = Bullshit()

    @Test
    fun testPositive() {
        bullshit.doAction(message(("Ho rubato 12.5 bs alla Campy")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("12.5 bullshit corrispondono a ").endsWith(" pregiati euro.")
    }

    @Test
    fun testPositive_2() {
        bullshit.doAction(message(("Ho rubato 12.5bullshit alla Campy")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("12.5 bullshit corrispondono a ").endsWith(" pregiati euro.")
    }

}