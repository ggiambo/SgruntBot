package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Tappeto
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class TappetoTest : BaseTest() {

    private val tappeto = Tappeto(botUtils, botConfig)

    @Test
    fun testPositive() {
         val message = message("!tappeto malattia e tutto il FdT")
        tappeto.handle(message)

        Assertions.assertThat(actionResponses).hasSize(1)
        Assertions.assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(actionResponses.first().message)
            .isEqualTo("Pippo manda malattia e tutto il FdT al tappeto!")
        Assertions.assertThat(actionResponses.first().inputFile!!.mediaName).isEqualTo("tappeto.jpg")
    }
}