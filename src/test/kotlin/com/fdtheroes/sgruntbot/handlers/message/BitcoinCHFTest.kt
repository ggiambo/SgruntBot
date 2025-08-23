package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BitcoinCHFTest : BaseTest() {

    private val bitcoinCHF = BitcoinCHF(botUtils, botConfig, mapper)

    @Test
    fun testPositive() {
        val message = message("!btcc")
        bitcoinCHF.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("Il buttcoin vale ").endsWith(" denti d'oro")
    }

    @Test
    fun testNegative() {
        val message = message("!btcc__")
        bitcoinCHF.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}