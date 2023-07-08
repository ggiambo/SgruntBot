package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction

internal class CanzoneTest : BaseTest() {

    private val canzone = Canzone()

    //@Test
    fun testPositive() {
        val ctx = actionContext("!canzone 2 Seconds Video")
        canzone.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Audio)
        assertThat(ctx.actionResponses.first().inputFile!!.mediaName).isEqualTo("2_Second_Video.mp3")
    }
}