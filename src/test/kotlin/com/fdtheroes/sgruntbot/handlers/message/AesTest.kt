package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AesTest : BaseTest() {

    val aes = Aes(botUtils, botConfig)

    @Test
    fun testEncrypt() {
        val message = message("!aes mySecretKey testo segretissimo da da codificare")
        aes.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
    }

    @Test
    fun testDencrypt() {
        val message = message("!aesd mySecretKey L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
        aes.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("testo segretissimo da da codificare")
    }

}