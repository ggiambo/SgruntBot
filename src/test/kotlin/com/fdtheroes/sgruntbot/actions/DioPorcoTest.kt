package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class DioPorcoTest : BaseTest() {

    private val dioPorco = DioPorco(botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        dioPorco.doAction(message(("\tdio porco")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Che mi tocca sentire!")
    }

    @Test
    fun testPositive_2() {
        botConfig.pignolo = true
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
