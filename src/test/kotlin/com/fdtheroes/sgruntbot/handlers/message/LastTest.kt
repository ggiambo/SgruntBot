package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepositoryHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LastTest : BaseTest() {

    private val nameValuePairRepository = NameValuePairRepositoryHelper().nameValuePairRepository()
    private val last = Last(nameValuePairRepository, botUtils, botConfig)

    @Test
    fun testPositive() {
        val user = user(Users.F)
        val lastAuthor = NameValuePair(NameValuePair.NameValuePairName.LAST_AUTHOR, user.id.toString())
        nameValuePairRepository.save(lastAuthor)
        val message = message("!last")
        last.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).contains("<a href=\"tg://user?id=259607683\">Username_259607683</a>")
    }

}
