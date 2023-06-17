package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class AesTest : BaseTest() {

    val aes = Aes()

    @Test
    fun testEncrypt() {
        aes.doAction(actionContext("!aes mySecretKey testo segretissimo da da codificare"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
    }

    @Test
    fun testDencrypt() {
        aes.doAction(
            actionContext("!aesd mySecretKey L9dQ8mCVcA3EyHZN+X+Pi2CYZnGnRXE3ol3qSmYnkgjnfYdVGGcLraGEgVXJYsto")
        )

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("testo segretissimo da da codificare")
    }

}