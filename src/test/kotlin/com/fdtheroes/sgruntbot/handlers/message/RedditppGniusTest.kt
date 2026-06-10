package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

class RedditppGniusTest : BaseTest() {

    private val redditppGnius = RedditppGnius(botUtils, botConfig, mapper)

    @Test
    fun getGnius() {
        val output = this.javaClass.classLoader.getResourceAsStream("redditpp.json")
        doReturn(output.reader().readText())
            .whenever(botUtils).textFromURL(any(), any(), any(), any(), any())


        val res = redditppGnius.getGnius("dummy")

        assertThat(res).hasSize(3)
        assertThat(res[0]).isEqualTo("\uD83D\uDCF0 - <a href='https://www.phoronix.com/news/AMDGPU-DC-Preps-HDMI-Comp-Test'>AMDGPU Linux Driver Preps For HDMI 2.1 Compliance Testing</a>")
        assertThat(res[1]).isEqualTo("\uD83D\uDCF0 - <a href='https://reddit.com/r/linux/comments/1u1zrws/'>I Sped up mt76 USB WIFI ~1.5x with a kernel patch that has been acked for mainline</a>")
        assertThat(res[2]).isEqualTo("\uD83D\uDCF0 - <a href='https://reddit.com/r/linux/comments/1u1yp7z/'>Terminal Tower of Hanoi, in Bash</a>")
    }

}