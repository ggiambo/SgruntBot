package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class ChiEraTest : BaseTest() {

    private val usersService = mock<UsersService>()
    private val chiEra = ChiEra(usersService, botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.lastSuper = user()
        whenever(usersService.getAllUsers()).doReturn(listOf(botConfig.lastSuper!!))
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("""<a href="tg://user?id=42">Pippo</a>""")
    }

    @Test
    fun testPositive_2() {
        botConfig.lastSuper = user(42, userName = "", firstName = "Topopippo")
        whenever(usersService.getAllUsers()).doReturn(listOf(botConfig.lastSuper!!))
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("""<a href="tg://user?id=42">Topopippo</a>""")
    }

    @Test
    fun testNegative() {
        botConfig.lastSuper = null
        val message = message("!chiera")
        chiEra.handle(message)

        assertThat(actionResponses).hasSize(0)
    }
}
