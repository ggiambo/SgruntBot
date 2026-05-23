package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.persistence.StatsService
import com.fdtheroes.sgruntbot.utils.PieChartUtils
import com.fdtheroes.sgruntbot.utils.StatsUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.awt.image.BufferedImage
import java.time.LocalDate
import javax.imageio.ImageIO

internal class StatsTest : BaseTest() {

    val dummyDate = LocalDate.of(2022, 8, 10)

    var id: Long = 1
    val monthStats = listOf(
        Stats(Users.IL_VINCI.id, dummyDate, 13, id++),
        Stats(Users.F.id, dummyDate, 15, id++),
        Stats(Users.DA_DA_212.id, dummyDate, 21, id++),
    )

    val statsService = mock<StatsService> {
        on { getStatsThisMonth() } doReturn (monthStats)
    }

    val stats = Stats(
        botUtils,
        botConfig,
        statsService,
        StatsUtil(statsService, PieChartUtils(botUtils)),
    )

    @Test
    fun test_positive() {
        val message = message("!stats g")
        stats.handle(message)

        assertThat(actionResponses).hasSize(1)
        val image = ImageIO.read(actionResponses.first().inputFile!!.newMediaStream)

        assertThat(image.type).isEqualTo(BufferedImage.TYPE_4BYTE_ABGR)
        assertThat(image.width).isEqualTo(1024)
        assertThat(image.height).isEqualTo(768)
    }

    @Test
    fun test_positive_uppercase() {
        val message = message("!stats M")
        stats.handle(message)

        assertThat(actionResponses).hasSize(1)
        val image = ImageIO.read(actionResponses.first().inputFile!!.newMediaStream)

        assertThat(image.type).isEqualTo(BufferedImage.TYPE_4BYTE_ABGR)
        assertThat(image.width).isEqualTo(1024)
        assertThat(image.height).isEqualTo(768)
    }

    @Test
    fun test_negative_with_help() {
        val message = message("!stats xy")
        stats.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("<b>!stats <i>tipo</i></b>")
    }

    @Test
    fun test_negative2() {
        val message = message("!statsOH")
        stats.handle(message)

        assertThat(actionResponses).isEmpty()
    }

    @Test
    fun test_negative_with_help2() {
        val message = message("!stats L")
        stats.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).startsWith("<b>!stats <i>tipo</i></b>")
    }

}