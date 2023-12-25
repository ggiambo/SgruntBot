package com.fdtheroes.sgruntbot.actions.stats

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
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
        Stats(Users.DA_DA212.id, dummyDate, 21, id++),
    )

    val statsService = mock<StatsService> {
        on { getStatsThisMonth() } doReturn (monthStats)
    }

    val stats = com.fdtheroes.sgruntbot.actions.Stats(
        statsService,
        StatsUtil(statsService, botUtils),
        botUtils
    )

    @Test
    fun test_positive() {
        val ctx = actionContext("!stats")
        stats.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        val image = ImageIO.read(ctx.actionResponses.first().inputFile!!.newMediaStream)

        assertThat(image.type).isEqualTo(BufferedImage.TYPE_3BYTE_BGR)
        assertThat(image.width).isEqualTo(1280)
        assertThat(image.height).isEqualTo(1024)
    }
}