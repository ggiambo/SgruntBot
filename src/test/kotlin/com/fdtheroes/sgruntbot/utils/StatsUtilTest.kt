package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.persistence.StatsRepository
import com.fdtheroes.sgruntbot.persistence.StatsService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.File
import java.time.LocalDate
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class StatsUtilTest : BaseTest() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val statsService = StatsService(
        mock<StatsRepository> {
            on { findStatsByStatDayBetween(any(), any()) } doReturn stats()
        }
    )

    private val statsUtil = StatsUtil(statsService, botUtils)

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun getStats() {
        val stats = statsUtil.getStats(42)
        val statsOut = this.javaClass.getResourceAsStream("/statsOut.png")

        //val out = stats.newMediaStream.readAllBytes()
        //File("src/test/resources/_statsOut.png").writeBytes(out)

        val actualOut = statsOut.readAllBytes()
        val expectedOut = stats.newMediaStream.readAllBytes()

        log.info(Base64.encode(actualOut))
        log.info(Base64.encode(expectedOut))

        Assertions.assertThat(actualOut).isEqualTo(expectedOut)
    }

    private fun stats(): List<Stats> {
        return (1..5).map {
            Stats(
                it.toLong(),
                LocalDate.now(),
                it * 10
            )
        }
    }
}