package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class HalpTest : BaseTest() {

    private val halp = Halp(listOf(HasHalp { "Dummy Halp" }))

    @Test
    fun testPositive() {
        halp.doAction(message("!help"), sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).contains("Dummy Halp")
    }

}
