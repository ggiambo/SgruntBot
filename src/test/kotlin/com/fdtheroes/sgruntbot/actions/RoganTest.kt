package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class RoganTest : BaseTest() {

    private val rogan = Rogan()

    @Test
    fun testPositive() {
        val ctx = actionContext("a me rogan sta sulle balle")
        rogan.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Cheppalle! Yawn!")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext("stanno erogando cazzate")
        rogan.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }

}