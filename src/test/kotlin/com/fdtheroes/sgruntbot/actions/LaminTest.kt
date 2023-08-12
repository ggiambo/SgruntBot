package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LaminTest : BaseTest() {

    private val lamin = Lamin()

    @Test
    fun testPositive() {
        val ctx = actionContext("negraccio")
        lamin.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testPositive_2() {
        val ctx = actionContext("__negher++")
        lamin.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testNegative() {
        val ctx = actionContext("**negrini!!")
        lamin.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }

}