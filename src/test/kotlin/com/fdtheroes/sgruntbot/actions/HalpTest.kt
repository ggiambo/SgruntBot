package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Halp
import com.fdtheroes.sgruntbot.handlers.message.HasHalp
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class HalpTest : BaseTest() {

    private val halp = Halp(botUtils, botConfig, listOf(HasHalp { "Dummy Halp" }))

    @Test
    fun testPositive() {
        val message = message("!help")
        halp.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).contains("Dummy Halp")
    }

}
