package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BitcoinUSDTest : BaseTest() {

    private val bitcoinUSD = BitcoinUSD(botUtils, botConfig, mapper)

    @Test
    fun testPositive() {
        val message = message("!btc")
        bitcoinUSD.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("Il buttcoin vale ")
            .endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testPositive2() {
        val message = message("blah banf quanto vale un bitcoin? yadda yadda")
        bitcoinUSD.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("Il buttcoin vale ")
            .endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testNegative() {
        val message = message("!btce__")
        bitcoinUSD.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}