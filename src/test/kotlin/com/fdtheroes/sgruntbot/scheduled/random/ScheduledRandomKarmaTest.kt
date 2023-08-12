package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class ScheduledRandomKarmaTest : BaseTest() {

    private val usersService = mock<UsersService> {
        on { getAllActiveUsers(any()) } doReturn listOf(
            user(1, "uno"),
            user(2, "due"),
            user(3, "tre")
        )
    }
    private val randomKarma = ScheduledRandomKarma(usersService, mock(), botUtils, sgruntBot)

    @Test
    fun testRandomKarma() {
        botConfig.lastAuthor = user(Users.SHDX_T)
        randomKarma.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).startsWith("<a href=\"tg://user?id=")
        assertThat(actionResponse.message).contains(" in verità in verità ti dico: Sgrunty da, Sgrunty toglie.\n")
        assertThat(actionResponse.message).contains("Il tuo karma è ")
        assertThat(actionResponse.message).endsWith(" di 1.")
        assertThat(actionResponse.inputFile).isNull()
        assertThat(actionResponse.rispondi).isFalse()
    }
}
