package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PorcaMadonnaTest : BaseTest() {

    private val porcaMadonna = PorcaMadonna(botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        val ctx = actionContext(("\tporca madonna"))
        porcaMadonna.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("...e tutti gli angeli in colonna!")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext(("copporca madonna "))
        porcaMadonna.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }

}
