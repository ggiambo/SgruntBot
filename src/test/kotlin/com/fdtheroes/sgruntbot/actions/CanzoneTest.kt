package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Canzone_Old
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat

internal class CanzoneTest : BaseTest() {

    private val canzone = Canzone_Old(botUtils, botConfig)

    //@Test
    fun testPositive() {
        val message = message("!canzone 2 Seconds Video")
        canzone.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Audio)
        assertThat(actionResponses.first().inputFile!!.mediaName).isEqualTo("2_Second_Video.mp3")
    }
}