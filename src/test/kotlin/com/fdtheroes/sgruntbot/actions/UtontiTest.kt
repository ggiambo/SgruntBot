package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.Utonti
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class UtontiTest : BaseTest() {

    @Test
    fun utonti_negative() {
        val utonti = Utonti(botUtils, botConfig, usersService())
        val message = message("Blah Banf", user(Users.F))

        utonti.handle(message)

        assertThat(actionResponses).hasSize(0)
    }

    @Test
    fun utonti_positive() {
        val utonti = Utonti(botUtils, botConfig, usersService())
        val message = message("!utonti", user(Users.F))

        utonti.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("Utonti di questa ciat")
        assertThat(actionResponses.first().message).contains("42: Pippo")
        assertThat(actionResponses.first().message).contains("104278889: IL_VINCI")

    }


    private fun usersService(): UsersService {
        return mock {
            on { getAllUsers() } doReturn listOf(
                user(),
                user(Users.IL_VINCI)
            )
        }
    }

}