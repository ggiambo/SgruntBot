package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class LastTest : ActionTest() {

    private val last = Last()

    @Test
    fun testPositive() {
        last.doAction(message("!last"), Context().apply { lastAuthor = BotUtils.Users.GENGY.name })

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).contains("GENGY")
    }

}