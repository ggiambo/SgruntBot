package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ChiEraTest : BaseTest() {

    private val chiEra = ChiEra(botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.lastSuper = user()
        val ctx = actionContext("!chiera")
        chiEra.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("""<a href="tg://user?id=42">Pippo</a>""")
    }

    @Test
    fun testPositive_2() {
        botConfig.lastSuper = user(42, userName = "", firstName = "Topopippo")
        val ctx = actionContext("!chiera")
        chiEra.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("""<a href="tg://user?id=42">Topopippo</a>""")
    }

    @Test
    fun testNegative() {
        botConfig.lastSuper = null
        val ctx = actionContext("!chiera")
        chiEra.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }
}
