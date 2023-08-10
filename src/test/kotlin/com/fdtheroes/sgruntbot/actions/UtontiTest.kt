package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock

class UtontiTest : BaseTest() {

    @Test
    fun utonti_negative() {
        val utonti = Utonti(botUtils, usersService())
        val ctx = actionContext("Blah Banf", user(Users.F))

        utonti.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(0)
    }

    @Test
    fun utonti_positive() {
        val utonti = Utonti(botUtils, usersService())
        val ctx = actionContext("!utonti", user(Users.F))

        utonti.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("Utonti di questa ciat")
        assertThat(ctx.actionResponses.first().message).contains("42: Pippo")
        assertThat(ctx.actionResponses.first().message).contains("104278889: IL_VINCI")

    }


    private fun usersService(): UsersService {
        return mock {
            on { getAllUsers(isA()) } doReturn listOf(
                user(),
                user(Users.IL_VINCI)
            )
        }
    }

}