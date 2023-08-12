package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PorcoDioTest : BaseTest() {

    private val porcoDio = PorcoDio(botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        val ctx = actionContext(("\tporco dio"))
        porcoDio.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("E la madooonna!")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext(("porco dioh! "))
        porcoDio.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }

}
