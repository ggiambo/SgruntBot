package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDateTime

internal class CheOreSonoTest : BaseTest() {

    @Test
    fun testPositive_prima_di_mezzogiorno() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 11, 44) }
        val ctx = actionContext("non so che ore sono a dire il vero")
        cheOreSono.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).hasSize(1)
        Assertions.assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(ctx.actionResponses.first().message).isEqualTo("mezzogiorno meno venti (precisamente 11:44 ok?)")
    }

    @Test
    fun testPositive_dopo_mezzogiorno() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 12, 17) }
        val ctx = actionContext("non so che ore sono a dire il vero")
        cheOreSono.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).hasSize(1)
        Assertions.assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(ctx.actionResponses.first().message).isEqualTo("mezzogiorno e un quarto (precisamente 12:17 ok?)")
    }

    @Test
    fun testPositive_prima_di_mezzanotte() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 23, 44) }
        val ctx = actionContext("non so che ore sono a dire il vero")
        cheOreSono.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).hasSize(1)
        Assertions.assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(ctx.actionResponses.first().message).isEqualTo("mezzanotte meno venti (precisamente 23:44 ok?)")
    }

    @Test
    fun testPositive_dopo_mezzanotte() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 0, 17) }
        val ctx = actionContext("non so che ore sono a dire il vero")
        cheOreSono.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).hasSize(1)
        Assertions.assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(ctx.actionResponses.first().message).isEqualTo("mezzanotte e un quarto (precisamente 00:17 ok?)")
    }
}