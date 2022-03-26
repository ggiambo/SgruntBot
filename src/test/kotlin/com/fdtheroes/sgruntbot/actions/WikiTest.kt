package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class WikiTest : ActionTest() {

    private val wiki = Wiki(botUtils)

    @Test
    fun testPositive() {
        wiki.doAction(message("!wiki poesia giambica"), sgruntBot)

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text)
            .startsWith("La poesia giambica era un tipo di poesia ")
            .endsWith("https://it.wikipedia.org/wiki/Poesia+giambica")
    }

}