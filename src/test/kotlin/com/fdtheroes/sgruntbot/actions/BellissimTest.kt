package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Bellissim
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BellissimTest : BaseTest() {

    private val bellissim = Bellissim(botUtils, botConfig)

    @Test
    fun testPositive() {
        val message = message("XYZ_bEllISSimo_123")
        bellissim.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses[0].type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("IO sono bellissimo! ....")
    }

    @Test
    fun testNegative() {
        val message = message("XYZ_quack_123")
        bellissim.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}