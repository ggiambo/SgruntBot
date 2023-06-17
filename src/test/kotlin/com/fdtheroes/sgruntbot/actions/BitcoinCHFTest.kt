package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class BitcoinCHFTest : BaseTest() {

    private val bitcoinCHF = BitcoinCHF(botUtils, mapper)

    @Test
    fun testPositive() {
        bitcoinCHF.doAction(actionContext(("!btcc")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("Il buttcoin vale ").endsWith(" denti d'oro")
    }

    @Test
    fun testNegative() {
        bitcoinCHF.doAction(actionContext(("!btcc__")))

        assertThat(botArguments).hasSize(0)
    }
}