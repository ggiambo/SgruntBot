package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.Sgrunt
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SgruntTest : BaseTest() {

    private val sgrunt = Sgrunt(botUtils, botConfig)

    @Test
    fun testPositive() {
         val message = message("sgrunty")
        sgrunt.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testPositive_1() {
         val message = message(text = "sgruntbot", from = user(id = Users.DANIELE.id))
        sgrunt.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Ciao papà!")
    }

}