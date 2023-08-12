package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TestTest : BaseTest() {

    private val test = Test(botUtils)

    @Test
    fun testPositive() {
        val ctx = actionContext("!test")
        test.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("""<a href="tg://user?id=42">Pippo</a>: toast <pre>test</pre>""")
    }

}