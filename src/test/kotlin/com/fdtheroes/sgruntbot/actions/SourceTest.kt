package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Source
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SourceTest : BaseTest() {

    private val source = Source(botUtils, botConfig)

    @Test
    fun testPositive() {
         val message = message("!source")
        source.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("http://github.com/ggiambo/SgruntBot")
    }

}