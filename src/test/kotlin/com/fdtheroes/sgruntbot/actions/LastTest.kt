package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class LastTest : ActionTest() {

    private val last = Last()

    @Test
    fun testPositive() {
        last.doAction(message("!last"), Context().apply { lastAuthor = BotUtils.Users.GENGY.name })

        Assertions.assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        Assertions.assertThat(sendMessage.text).contains("GENGY")
    }

}