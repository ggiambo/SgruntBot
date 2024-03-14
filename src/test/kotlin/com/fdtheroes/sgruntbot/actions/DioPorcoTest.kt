package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.DioPorco
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DioPorcoTest : BaseTest() {

    private val dioPorco = DioPorco(botUtils, botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        val message = message("\tdio porco")
        dioPorco.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Che mi tocca sentire!")
    }

    @Test
    fun testPositive_2() {
        botConfig.pignolo = true
        val message = message("dio cane\n")
        dioPorco.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Che mi tocca sentire!")
    }


    @Test
    fun testNegative() {
        val message = message("condio cane ")
        dioPorco.handle(message)

        assertThat(actionResponses).isEmpty()
    }

}
