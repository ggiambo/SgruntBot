package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class AesTest : BaseTest() {

    val aes = Aes()

    @Test
    fun testEncrypt() {
        val ctx = actionContext("!aes mySecretKey testo segretissimo da da codificare")
        aes.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
    }

    @Test
    fun testDencrypt() {
        val ctx = actionContext("!aesd mySecretKey L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
        aes.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("testo segretissimo da da codificare")
    }

}