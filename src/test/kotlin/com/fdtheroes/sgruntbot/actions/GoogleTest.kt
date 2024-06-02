package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Google
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GoogleTest : BaseTest() {

    private val google = Google(botUtils, botConfig)

    @Test
    fun testPositive() {
        val message = message("!google Sgrunt bot")
        google.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("""Cercatelo con <a href="https://www.google.com/search?q=Sgrunt bot">google</a> ritardato!™""")
    }

}