package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.Slogan
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ScheduledRandomSloganTest : BaseTest() {

    private val slogan = Slogan(botUtils)
    private val randomSlogan = ScheduledRandomSlogan(slogan, sgruntBot, botConfig)

    @Test
    fun testdRandomSlogan() {
        botConfig.lastAuthor = user(Users.SHDX_T)
        randomSlogan.execute()

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(sgruntBot, times(1)).messaggio(argumentCaptor.capture())
        val actionResponse = argumentCaptor.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).contains("<a href=\"tg://user?id=68714652\">SHDX_T</a> ")
        assertThat(actionResponse.inputFile).isNull()
        assertThat(actionResponse.rispondi).isFalse()
    }
}
