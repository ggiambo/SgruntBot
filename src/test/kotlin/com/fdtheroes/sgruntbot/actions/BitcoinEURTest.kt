package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.BitcoinEUR
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BitcoinEURTest : BaseTest() {

    private val bitcoinEUR = BitcoinEUR(botUtils, botConfig, mapper)

    @Test
    fun testPositive() {
        val message = message("!btce")
        bitcoinEUR.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("Il buttcoin vale ").endsWith(" EUR")
    }

    @Test
    fun testNegative() {
        val message = message("!btce__")
        bitcoinEUR.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}