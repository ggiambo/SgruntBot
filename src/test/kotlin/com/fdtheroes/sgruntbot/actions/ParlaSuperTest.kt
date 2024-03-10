package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.ParlaSuper
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParlaSuperTest : BaseTest() {

    private val parlaSuper = ParlaSuper(botUtils, botConfig)

    @Test
    fun testPositive() {
         val message = message(text = "!parlaSuper questo bot è stupendo!", from = user(id = Users.IL_VINCI.id))
        parlaSuper.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("questo bot è stupendo!")
    }

    @Test
    fun testNegative() {
         val message = message("!parlaSuper questo bot è stupendo!")
        parlaSuper.handle(message)

        assertThat(actionResponses).isEmpty()
    }

}