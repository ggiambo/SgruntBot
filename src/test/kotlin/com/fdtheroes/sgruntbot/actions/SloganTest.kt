package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SloganTest : BaseTest() {

    private val slogan = Slogan(botUtils)

    @Test
    fun testPositive() {
        val ctx = actionContext("!slogan la cacca molle")
        slogan.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).containsIgnoringCase("la cacca molle")
    }

    @Test
    fun testFetchSloganUser() {
        val user = user(Users.SHDX_T)
        val slogan = slogan.fetchSlogan(user)

        assertThat(slogan).contains("""<a href="tg://user?id=68714652">SHDX_T</a>""")
    }

}