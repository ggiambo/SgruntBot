package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class DioPorcoTest : BaseTest() {

    private val dioPorco = DioPorco(botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        val ctx = actionContext(("\tdio porco"))
        dioPorco.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Che mi tocca sentire!")
    }

    @Test
    fun testPositive_2() {
        botConfig.pignolo = true
        val ctx = actionContext(("dio cane\n"))
        dioPorco.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Che mi tocca sentire!")
    }


    @Test
    fun testNegative() {
        val ctx = actionContext(("condio cane "))
        dioPorco.doAction(ctx)

        assertThat(ctx.actionResponses).isEmpty()
    }

}
