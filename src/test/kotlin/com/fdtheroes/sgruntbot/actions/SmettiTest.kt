package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDateTime

internal class SmettiTest : BaseTest() {

    private val smetti = Smetti(botConfig)

    @ParameterizedTest
    @ValueSource(
        strings = [
            "sgrunty ora smetti",
            "blahbanfbot ora smetti",
            "sgruntbot smettila",
            "blahbanf smettila",
            "@sgrunty smetti!",
            "@blahbanfbot smettilaaaa!",
            "@sgrunt dismetti",
        ]
    )
    fun testPositive(message: String) {
        val ctx = actionContext(message)
        smetti.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Ok, sto zitto 5 minuti. :(")
        assertThat(botConfig.pausedTime?.isAfter(LocalDateTime.now()))
    }

    @ParameterizedTest
    @ValueSource(
        strings = [
            "sgruntollo smetti",
            "sgruntavo sulle colline",
            "sgruntsmetti",
        ]
    )
    fun testNegative(message: String) {
        val ctx = actionContext(message)
        smetti.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }

}
