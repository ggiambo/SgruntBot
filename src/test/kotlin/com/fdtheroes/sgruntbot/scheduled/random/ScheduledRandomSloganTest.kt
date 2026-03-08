package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.Slogan
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepositoryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class ScheduledRandomSloganTest : BaseTest() {

    private val slogan = Slogan(botUtils, botConfig ,mapper)
    private val nameValuePairRepository = NameValuePairRepositoryHelper().nameValuePairRepository()
    private val randomSlogan = ScheduledRandomSlogan(botUtils, nameValuePairRepository, slogan)

    @Test
    fun testdRandomSlogan() {
        val user =  user(Users.SHDX_T)
        val lastAuthor = NameValuePair(NameValuePair.NameValuePairName.LAST_AUTHOR, user.id.toString())
        nameValuePairRepository.save(lastAuthor)
        randomSlogan.execute()

        val argumentCaptor = argumentCaptor<ActionResponse, Boolean>()
        verify(botUtils, times(1)).messaggio(argumentCaptor.first.capture(), any())
        val actionResponse = argumentCaptor.first.firstValue
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).contains("<a href=\"tg://user?id=68714652\">Username_68714652</a>")
        assertThat(actionResponse.inputFile).isNull()
    }
}
