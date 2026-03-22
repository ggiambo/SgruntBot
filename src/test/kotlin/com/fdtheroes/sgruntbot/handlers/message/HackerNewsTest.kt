package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HackerNewsTest : BaseTest() {

    private val hackerNews = HackerNews(botUtils, botConfig, mapper)

    @Test
    fun testNegative() {
        val message = message("!proot")
        hackerNews.handle(message)

        assertThat(actionResponses).isEmpty()
    }

    @Test
    fun testPositive() {
        val message = message("!hn")
        hackerNews.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("Hacker News Top Stories:")
        val entries = actionResponses.first().message.split("\r").drop(1)
        assertThat(entries).allMatch { it.startsWith("  \uD83D\uDCE2 <a href=\"") }
    }

}