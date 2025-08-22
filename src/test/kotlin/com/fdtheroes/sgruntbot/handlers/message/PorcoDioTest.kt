package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PorcoDioTest : BaseTest() {

    private val porcoDio = PorcoDio(botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        val message = message("\tporco dio")
        porcoDio.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("E la madooonna!")
    }

    @Test
    fun testNegative() {
        val message = message("porco dioh! ")
        porcoDio.handle(message)

        assertThat(actionResponses).isEmpty()
    }

}
