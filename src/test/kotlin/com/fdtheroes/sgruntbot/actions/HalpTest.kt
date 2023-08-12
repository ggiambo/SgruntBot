package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HalpTest : BaseTest() {

    private val halp = Halp(listOf(HasHalp { "Dummy Halp" }))

    @Test
    fun testPositive() {
        val ctx = actionContext("!help")
        halp.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).contains("Dummy Halp")
    }

}
