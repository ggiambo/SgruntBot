package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDateTime

internal class SmettiTest : BaseTest() {

    private val smetti = Smetti(sgruntBot, botConfig)

    @ParameterizedTest
    @ValueSource(strings = ["sgrunty ora smetti", "sgruntbot smettila", "@sgrunty smetti!"])
    fun testPositive(message: String) {
        smetti.doAction(message(message))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Ok, sto zitto 5 minuti. :(")
        assertThat(botConfig.pausedTime?.isAfter(LocalDateTime.now()))
    }

}
