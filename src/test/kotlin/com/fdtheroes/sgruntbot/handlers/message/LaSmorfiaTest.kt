package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LaSmorfiaTest : BaseTest() {

    private val laSmorfia = LaSmorfia(botUtils, botConfig, mapper)

    @Test
    fun test_outOfRange() {
        laSmorfia.handle(message("!smorfia 99"))

        assertThat(actionResponses).hasSize(0)
    }

    @Test
    fun test_positive() {
        laSmorfia.handle(message("!smorfia 12"))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isNotEmpty
        assertThat(actionResponses.first().message).isEqualTo("\uD83C\uDDEE\uD83C\uDDF9 12: ‘e surdate (i soldati) \uD83E\uDD0C")
    }
}