package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BitcoinCHFTest : BaseTest() {

    private val bitcoinCHF = BitcoinCHF(botUtils, mapper)

    @Test
    fun testPositive() {
        val ctx = actionContext(("!btcc"))
        bitcoinCHF.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("Il buttcoin vale ").endsWith(" denti d'oro")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext(("!btcc__"))
        bitcoinCHF.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }
}