package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class PorcaMadonnaTest : BaseTest() {

    private val porcaMadonna = PorcaMadonna(sgruntBot, botConfig)

    @Test
    fun testPositive() {
        botConfig.pignolo = true
        porcaMadonna.doAction(message(("\tporca madonna")))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("...e tutti gli angeli in colonna!")
    }

    @Test
    fun testNegative() {
        porcaMadonna.doAction(message(("copporca madonna ")))

        assertThat(botArguments).isEmpty()
    }

}
