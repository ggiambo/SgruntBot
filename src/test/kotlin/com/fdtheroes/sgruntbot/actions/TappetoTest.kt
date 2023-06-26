package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto

internal class TappetoTest : BaseTest() {

    private val tappeto = Tappeto()

    @Test
    fun testPositive() {
        val ctx = actionContext("!tappeto malattia e tutto il FdT")
        tappeto.doAction(ctx)

        Assertions.assertThat(ctx.actionResponses).hasSize(1)
        Assertions.assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Photo)
        Assertions.assertThat(ctx.actionResponses.first().message).isEqualTo("Pippo manda malattia e tutto il FdT al tappeto!")
        Assertions.assertThat(ctx.actionResponses.first().inputFile!!.mediaName).isEqualTo("tappeto.jpg")
    }
}