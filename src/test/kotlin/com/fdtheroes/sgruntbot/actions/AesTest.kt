package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class AesTest : ActionTest() {

    val aes = Aes()

    @Test
    fun testEncrypt() {
        aes.doAction(message("!aes mySecretKey testo segretissimo da da codificare"), Context())

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
    }

    @Test
    fun testDencrypt() {
        aes.doAction(message("!aesd mySecretKey L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto"), Context())

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("testo segretissimo da da codificare")
    }

}