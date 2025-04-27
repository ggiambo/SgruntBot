package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.Last
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LastTest : BaseTest() {

    private val last = Last(botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.lastAuthor = user(Users.F)
        val message = message("!last")
        last.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).contains("F")
    }

}
