package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParlaSuperTest : BaseTest() {

    private val parlaSuper = ParlaSuper(botConfig)

    @Test
    fun testPositive() {
        val ctx = actionContext(text = "!parlaSuper questo bot è stupendo!", from = user(id = Users.AVVE.id))
        parlaSuper.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("questo bot è stupendo!")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext("!parlaSuper questo bot è stupendo!")
        parlaSuper.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }

}