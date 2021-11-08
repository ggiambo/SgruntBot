package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class TestTest : ActionTest() {

    private val test = com.fdtheroes.sgruntbot.actions.Test()

    @Test
    fun testPositive() {
        test.doAction(message("!test"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("""<a href="tg://user?id=42">Pippo</a>: toast <pre>test</pre>""")
    }

}