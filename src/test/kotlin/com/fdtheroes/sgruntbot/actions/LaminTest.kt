package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Lamin
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class LaminTest : BaseTest() {

    private val lamin = Lamin(botUtils, botConfig)

    @Test
    fun testPositive() {
         val message = message("negraccio")
        lamin.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testPositive_2() {
         val message = message("__negher++")
        lamin.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isNotEmpty
    }

    @Test
    fun testNegative() {
         val message = message("**negrini!!")
        lamin.handle(message)

        assertThat(actionResponses).isEmpty()
    }

}