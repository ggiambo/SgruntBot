package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RespectTest : ActionTest() {

    private val respect = Respect(botUtils)

    @Test
    fun testPositive() {
        respect.doAction(
            message(
                text = "F",
                replyToMessage = message(text = "whatever", from = user(id = Users.AVVE.id, userName = "AvveFaTutti"))
            ),
            sgruntBot
        )

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text)
            .isEqualTo("""Baciamo le mani Don <a href="tg://user?id=10427888">AvveFaTutti</a>""")
    }

    @Test
    fun testNegative() {
        respect.doAction(message("F..anculo"), sgruntBot)

        Assertions.assertThat(botArguments).isEmpty()
    }
}