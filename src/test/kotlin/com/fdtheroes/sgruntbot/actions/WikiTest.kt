package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class WikiTest : BaseTest() {

    private val wiki = Wiki(botUtils, mapper)

    @Test
    fun testPositive() {
        val ctx = actionContext("!wiki poesia giambica")
        wiki.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message)
            .startsWith("La poesia giambica era un tipo di poesia ")
            .endsWith("https://it.wikipedia.org/wiki/Poesia_giambica")
    }

    @Test
    fun testPositive_accento() {
        val ctx = actionContext("!wiki fosse ardenne")
        wiki.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message)
            .startsWith("Fossé è un comune francese di 54 abitanti ")
            .endsWith("https://it.wikipedia.org/wiki/Foss%C3%A9_(Ardenne)")
    }

    @Test
    fun testNegative() {
        val ctx = actionContext("!wiki barzuffo")
        wiki.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Non c'è.")
    }

}
