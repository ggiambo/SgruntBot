package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.time.LocalDateTime

internal class SmettiTest : BaseTest() {

    private val smetti = Smetti(botUtils, botConfig)

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
        smetti.handle(message(message))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Ok, sto zitto 5 minuti. :(")
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
        smetti.handle(message(message))

        assertThat(actionResponses).hasSize(0)
    }

}
