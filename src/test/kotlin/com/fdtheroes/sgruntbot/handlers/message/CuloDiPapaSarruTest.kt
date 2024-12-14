package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.scheduled.random.ScheduledCuloDiPapaSarru
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CuloDiPapaSarruTest : BaseTest() {

    private val scheduledCuloDiPapaSarru = ScheduledCuloDiPapaSarru(botUtils)
    private val culoDiPapaSarru = CuloDiPapaSarru(botUtils, botConfig, scheduledCuloDiPapaSarru)

    @Test
    fun testVangelo() {
        culoDiPapaSarru.handle(message("!vangelo"))

        assertThat(actionResponses).isNotEmpty()
        actionResponses.forEach {
            assertThat(it.message.length).isLessThan(4096)
        }
    }
}