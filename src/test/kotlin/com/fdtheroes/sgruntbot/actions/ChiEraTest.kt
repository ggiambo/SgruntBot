package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.ChiEra
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ChiEraTest : BaseTest() {

    private val chiEra = ChiEra(botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.lastSuper = user()
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("""<a href="tg://user?id=42">Pippo</a>""")
    }

    @Test
    fun testPositive_2() {
        botConfig.lastSuper = user(42, userName = "", firstName = "Topopippo")
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("""<a href="tg://user?id=42">Topopippo</a>""")
    }

    @Test
    fun testNegative() {
        botConfig.lastSuper = null
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}
