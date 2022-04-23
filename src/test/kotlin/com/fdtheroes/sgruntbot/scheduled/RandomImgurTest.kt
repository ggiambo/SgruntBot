package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RandomImgurTest : BaseTest() {

    private val randomImgur = RandomImgur(botUtils, mapper, sgruntBot, botConfig)

    @Test
    fun testRandomImgur() {
        val messageText = randomImgur.getMessageText()

        assertThat(messageText).isNotEmpty
        assertThat(botArguments).isEmpty()
    }
}