package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class BitcoinEURTest : BaseTest() {

    private val bitcoinEUR = BitcoinEUR(botUtils, mapper)

    @Test
    fun testPositive() {
        val ctx = actionContext(("!btce"))
        bitcoinEUR.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("Il buttcoin vale ").endsWith(" EUR")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext(("!btce__"))
        bitcoinEUR.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }
}