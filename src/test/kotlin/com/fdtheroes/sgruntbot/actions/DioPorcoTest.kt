package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class DioPorcoTest : ActionTest() {

    private val dioPorco = DioPorco()

    @Test
    fun testPositive() {
        Context.pignolo = true
        dioPorco.doAction(message(("\tdio porco")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Che mi tocca sentire!")
    }

    @Test
    fun testPositive_2() {
        Context.pignolo = true
        dioPorco.doAction(message(("dio cane\n")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Che mi tocca sentire!")
    }


    @Test
    fun testNegative() {
        dioPorco.doAction(message(("condio cane ")))

        assertThat(botArguments).isEmpty()
    }

}