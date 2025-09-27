package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FortuneTest : BaseTest() {

    private val fortune = object : Fortune(botUtils, botConfig) {
        override fun getFortune(): String {
            return "Fake fortune, just for testing"
        }
    }

    @Test
    fun testPositive() {
        val message = message("!fortune")
        fortune.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testPositive_2() {
        val message = message("!quote")
        fortune.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isNotEmpty
    }
}