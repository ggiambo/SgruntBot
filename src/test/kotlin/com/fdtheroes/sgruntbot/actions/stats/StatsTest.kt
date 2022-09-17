package com.fdtheroes.sgruntbot.actions.stats

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.persistence.Stats
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import java.awt.image.BufferedImage
import java.time.LocalDate
import javax.imageio.ImageIO

internal class StatsTest : BaseTest() {

    val dummyDate = LocalDate.of(2022, 8, 10)

    var id: Long = 1
    val monthStats = listOf(
        Stats(id++, Users.AVVE.id, dummyDate, 13),
        Stats(id++, Users.GENGY.id, dummyDate, 15),
        Stats(id++, Users.DADA.id, dummyDate, 21),
    )

    val stats = com.fdtheroes.sgruntbot.actions.Stats(
        mock {
            on { getStatsThisMonth() } doReturn (monthStats)
        },
        botUtils
    )

    @Test
    fun test_positive() {
        stats.doAction(
            message("!stats"),
            sgruntBot
        )

        assertThat(botArguments).hasSize(1)
        val sendPhoto = botArguments[0] as SendPhoto
        val image = ImageIO.read(sendPhoto.photo.newMediaStream)

        assertThat(image.type).isEqualTo(BufferedImage.TYPE_3BYTE_BGR)
        assertThat(image.width).isEqualTo(1280)
        assertThat(image.height).isEqualTo(1024)
    }
}