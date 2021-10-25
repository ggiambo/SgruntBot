package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class BitcoinEURTest : ActionTest() {

    private val bitcoinEUR = BitcoinEUR()

    @Test
    fun testPositive() {
        bitcoinEUR.doAction(message(("!btce")), Context())

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).startsWith("Il buttcoin vale ").endsWith(" EUR")
    }

    @Test
    fun testNegative() {
        bitcoinEUR.doAction(message(("!btce__")), Context())

        Assertions.assertThat(botArguments).hasSize(0)
    }
}