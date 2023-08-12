package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BellissimTest : BaseTest() {

    private val bellissim = Bellissim()

    @Test
    fun testPositive() {
        val ctx = actionContext(("XYZ_bEllISSimo_123"))
        bellissim.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses[0].type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("IO sono bellissimo! ....")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext(("XYZ_quack_123"))
        bellissim.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }
}