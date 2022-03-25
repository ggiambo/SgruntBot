package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class ParolacceTest : ActionTest() {

    private val parolacce = Parolacce(sgruntBot, botUtils)

    @ParameterizedTest
    @ValueSource(strings = ["cazzone", "culona", " fica ", "stronzi", "merdah!"])
    fun testPositive(parolaccia: String) {
        Context.pignolo = true
        parolacce.doAction(message("blah banf $parolaccia yadda yadda"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).contains("""<a href="tg://user?id=42">Pippo</a>""")
    }
}