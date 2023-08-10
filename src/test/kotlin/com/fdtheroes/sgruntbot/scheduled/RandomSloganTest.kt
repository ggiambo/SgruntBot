package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.Slogan
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RandomSloganTest : BaseTest() {

    private val slogan = Slogan(botUtils)
    private val randomSlogan = RandomSlogan(slogan, sgruntBot, botConfig)

    @Test
    fun testRandomSlogan() {
        botConfig.lastAuthor = user(Users.SHDX_T)
        val messageText = randomSlogan.getMessageText()

        val seuLink = botUtils.getUserLink(botConfig.lastAuthor)
        assertThat(messageText).contains(seuLink)
    }
}
