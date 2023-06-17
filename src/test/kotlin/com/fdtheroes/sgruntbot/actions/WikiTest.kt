package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class WikiTest : BaseTest() {

    private val wiki = Wiki(botUtils, mapper)

    @Test
    fun testPositive() {
        wiki.doAction(message("!wiki poesia giambica"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text)
            .startsWith("La poesia giambica era un tipo di poesia ")
            .endsWith("https://it.wikipedia.org/wiki/Poesia_giambica")
    }

    @Test
    fun testPositive_accento() {
        wiki.doAction(message("!wiki fosse ardenne"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text)
            .startsWith("Fossé è un comune francese di 54 abitanti ")
            .endsWith("https://it.wikipedia.org/wiki/Foss%C3%A9_(Ardenne)")
    }

    @Test
    fun testNegative() {
        wiki.doAction(message("!wiki barzuffo"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).isEqualTo("Non c'è.")
    }

}
