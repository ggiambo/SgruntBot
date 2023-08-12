package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BitcoinUSDTest : BaseTest() {

    private val bitcoinUSD = BitcoinUSD(botUtils, mapper)

    @Test
    fun testPositive() {
        val ctx = actionContext(("!btc"))
        bitcoinUSD.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("Il buttcoin vale ")
            .endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testPositive2() {
        val ctx = actionContext(("blah banf quanto vale un bitcoin? yadda yadda"))
        bitcoinUSD.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("Il buttcoin vale ")
            .endsWith(" dolla uno. Io faccio amole lungo lungo. Io tanta volia.")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext(("!btce__"))
        bitcoinUSD.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }
}