package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class CoccolinoTest : BaseTest() {

    private val coccolino = Coccolino()

    @Test
    fun testPositive_1() {
        val ctx = actionContext("coccolo", from = user(id = Users.SUORA.id))
        coccolino.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Non chiamarmi così davanti a tutti!")
    }

    @Test
    fun testPositive_2() {
        val ctx = actionContext("coccolino", from = user(id = Users.SUORA.id))
        coccolino.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Non chiamarmi così davanti a tutti!")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext("coccolino", from = user(id = Users.GENGY.id))
        coccolino.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }
}