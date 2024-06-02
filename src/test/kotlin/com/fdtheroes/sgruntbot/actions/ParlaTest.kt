package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Parla
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParlaTest : BaseTest() {

    private val parla = Parla(botUtils, botConfig)

    @Test
    fun testPositive() {
        val message = message("!parla questo bot è stupendo!")
        parla.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Mi dicono di dire: questo bot è stupendo!")
    }

}