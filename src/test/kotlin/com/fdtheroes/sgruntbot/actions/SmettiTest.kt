package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDateTime

class SmettiTest : ActionTest() {

    private val smetti = Smetti()

    @ParameterizedTest
    @ValueSource(strings = ["sgrunty ora smetti", "sgruntbot smettila", "@sgrunty smetti!"])
    fun testPositive(message: String) {
        smetti.doAction(message(message), sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Ok, sto zitto 5 minuti. :(")
        assertThat(Context.pausedTime?.isBefore(LocalDateTime.now()))
    }

    @Test
    fun testPositive_DADA() {
        smetti.doAction(message(text = "@sgrunty smetti", from = user(id = Users.DADA.id)), sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Col cazzo!")
        assertThat(Context.pausedTime).isNull()
    }

}