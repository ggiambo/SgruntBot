package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class BitcoinUSDTest : ActionTest() {

    private val bitcoinUSD = BitcoinUSD()

    @Test
    fun testPositive() {
        bitcoinUSD.doAction(message(("!btc")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("Il buttcoin vale ")
            .endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testPositive2() {
        bitcoinUSD.doAction(message(("blah banf quanto vale un bitcoin? yadda yadda")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("Il buttcoin vale ")
            .endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testNegative() {
        bitcoinUSD.doAction(message(("!btce__")))

        assertThat(botArguments).hasSize(0)
    }
}