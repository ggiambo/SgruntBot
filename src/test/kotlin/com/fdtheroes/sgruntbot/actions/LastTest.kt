package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class LastTest : BaseTest() {

    private val last = Last(Slogan(botUtils), botConfig)

    @Test
    fun testPositive() {
        botConfig.lastAuthor = user(Users.GENGY)
        last.doAction(actionContext("!last"))

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).contains("GENGY")
    }

}
