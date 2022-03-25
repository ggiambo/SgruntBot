package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class SloganTest : ActionTest() {

    private val slogan = Slogan(sgruntBot, botUtils)

    @Test
    fun testPositive() {
        slogan.doAction(message("!slogan la cacca molle"))

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).containsIgnoringCase("la cacca molle")
    }

    @Test
    fun testFetchSloganUser() {
        val user = user(Users.SEU)
        val slogan = slogan.fetchSlogan(user)

        assertThat(slogan).contains("""<a href="tg://user?id=68714652">SEU</a>""")
    }

}