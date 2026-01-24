package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SloganTest : BaseTest() {

    private val slogan = Slogan(botUtils, botConfig, mapper)

    @Test
    fun testPositive() {
        val message = message("!slogan la cacca molle")
        slogan.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).containsIgnoringCase("la cacca molle")
    }

    @Test
    fun testFetchSloganUser() {
        val user = user(Users.SHDX_T)
        val slogan = slogan.fetchSlogan(user)

        assertThat(slogan).contains("""<a href="tg://user?id=68714652">SHDX_T</a>""")
    }

}