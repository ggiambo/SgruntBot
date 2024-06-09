package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.Coccolino
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CoccolinoTest : BaseTest() {

    private val coccolino = Coccolino(botUtils, botConfig)

    @Test
    fun testPositive_1() {
        val message = message("coccolo", from = user(id = Users.DANIELE.id))
        coccolino.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Non chiamarmi così davanti a tutti!")
    }

    @Test
    fun testPositive_2() {
        val message = message("coccolino", from = user(id = Users.DANIELE.id))
        coccolino.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Non chiamarmi così davanti a tutti!")
    }

    @Test
    fun testNegative() {
        val message = message("coccolino", from = user(id = Users.F.id))
        coccolino.handle(message)

        assertThat(actionResponses).isEmpty()
    }
}