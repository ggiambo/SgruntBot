package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class RespectTest : BaseTest() {

    private val respect = Respect(botUtils)

    @Test
    fun testPositive() {
        val ctx = actionContext(
            text = "F",
            replyToMessage = message(text = "whatever", from = user(id = Users.IL_VINCI.id, userName = "AvveFaTutti"))
        )
        respect.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).hasSize(1)
        Assertions.assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(ctx.actionResponses.first().message)
            .isEqualTo("""Baciamo le mani Don <a href="tg://user?id=104278889">AvveFaTutti</a>""")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext("F..anculo")
        respect.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).isEmpty()
    }
}