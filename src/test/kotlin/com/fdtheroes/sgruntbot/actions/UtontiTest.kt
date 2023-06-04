package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class UtontiTest : BaseTest() {

    @Test
    fun utonti_negative() {
        val utonti = Utonti(botUtils, botConfig, usersService())
        val message = message("Blah Banf", user(Users.GENGY))

        utonti.doAction(message, sgruntBot)

        assertThat(botArguments).hasSize(0)
    }

    @Test
    fun utonti_positive() {
        val utonti = Utonti(botUtils, botConfig, usersService())
        val message = message("!utonti", user(Users.GENGY))

        utonti.doAction(message, sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).startsWith("Utonti di questa ciat")
        assertThat(sendMessage.text).contains("42: Pippo")
        assertThat(sendMessage.text).contains("10427888: AVVE")

    }


    private fun usersService(): UsersService {
        return mock {
            on { getAllUsers(isA()) } doReturn listOf(
                user(),
                user(Users.AVVE)
            )
        }
    }

}