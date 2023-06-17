package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class SloganTest : BaseTest() {

    private val slogan = Slogan(botUtils)

    @Test
    fun testPositive() {
        slogan.doAction(message("!slogan la cacca molle"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        assertThat(sendMessage.text).containsIgnoringCase("la cacca molle")
    }

    @Test
    fun testFetchSloganUser() {
        val user = user(Users.SEU)
        val slogan = slogan.fetchSlogan(user)

        assertThat(slogan).contains("""<a href="tg://user?id=68714652">SEU</a>""")
    }

}