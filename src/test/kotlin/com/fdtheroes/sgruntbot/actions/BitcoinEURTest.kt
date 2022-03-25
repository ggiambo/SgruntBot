package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class BitcoinEURTest : ActionTest() {

    private val bitcoinEUR = BitcoinEUR(botUtils, sgruntBot)

    @Test
    fun testPositive() {
        bitcoinEUR.doAction(message(("!btce")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("Il buttcoin vale ").endsWith(" EUR")
    }

    @Test
    fun testNegative() {
        bitcoinEUR.doAction(message(("!btce__")))

        assertThat(botArguments).hasSize(0)
    }
}