package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RandomCuloDiPapaTest : BaseTest() {

    private val randomCuloDiPapa = RandomCuloDiPapa(botUtils, sgruntBot, botConfig)

    @Test
    fun testRandomCuloDiPapa() {
        val messageText = randomCuloDiPapa.getMessageText()

        assertThat(messageText).isNotEmpty
    }
}