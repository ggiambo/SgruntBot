package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.Fortune
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RandomFortuneTest : BaseTest() {

    private val fortune = Fortune(sgruntBot)
    private val randomFortune = RandomFortune(fortune, sgruntBot, botConfig)

    @Test
    fun testRandomFortune() {
        val messageText = randomFortune.getMessageText()

        assertThat(messageText).isNotEmpty
        assertThat(botArguments).isEmpty()
    }
}