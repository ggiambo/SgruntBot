package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PorcaMadonnaTest : BaseTest() {

    private val porcaMadonna = PorcaMadonna(botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        val message = message("\tporca madonna")
        porcaMadonna.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("...e tutti gli angeli in colonna!")
    }

    @Test
    fun testNegative() {
        val message = message("copporca madonna ")
        porcaMadonna.handle(message)

        assertThat(actionResponses).isEmpty()
    }

}
