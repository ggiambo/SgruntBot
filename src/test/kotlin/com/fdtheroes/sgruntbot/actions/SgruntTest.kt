package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SgruntTest : BaseTest() {

    private val sgrunt = Sgrunt()

    @Test
    fun testPositive() {
        val ctx = actionContext("sgrunty")
        sgrunt.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testPositive_1() {
        val ctx = actionContext(text = "sgruntbot", from = user(id = Users.DANIELE.id))
        sgrunt.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Ciao papà!")
    }

}