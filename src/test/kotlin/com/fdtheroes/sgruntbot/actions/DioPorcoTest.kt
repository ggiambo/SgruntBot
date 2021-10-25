package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class DioPorcoTest : ActionTest() {

    private val dioPorco = DioPorco()

    @Test
    fun testPositive() {
        dioPorco.doAction(message(("\tdio porco")), Context().apply { pignolo = true })

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("Che mi tocca sentire!")
    }

    @Test
    fun testPositive_2() {
        dioPorco.doAction(message(("dio cane\n")), Context().apply { pignolo = true })

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("Che mi tocca sentire!")
    }


    @Test
    fun testNegative() {
        dioPorco.doAction(message(("condio cane ")), Context())

        Assertions.assertThat(botArguments).isEmpty()
    }

}