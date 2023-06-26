package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParlaTest : BaseTest() {

    private val parla = Parla(botConfig)

    @Test
    fun testPositive() {
        val ctx = actionContext("!parla questo bot è stupendo!")
        parla.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Mi dicono di dire: questo bot è stupendo!")
    }

}