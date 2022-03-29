package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class CoccolinoTest : BaseTest() {

    private val coccolino = Coccolino()

    @Test
    fun testPositive_1() {
        coccolino.doAction(message("coccolo", from = user(id = Users.SUORA.id)), sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Non chiamarmi così davanti a tutti!")
    }

    @Test
    fun testPositive_2() {
        coccolino.doAction(message("coccolino", from = user(id = Users.SUORA.id)), sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Non chiamarmi così davanti a tutti!")
    }

    @Test
    fun testNegative() {
        coccolino.doAction(message("coccolino", from = user(id = Users.GENGY.id)), sgruntBot)

        assertThat(botArguments).isEmpty()
    }
}