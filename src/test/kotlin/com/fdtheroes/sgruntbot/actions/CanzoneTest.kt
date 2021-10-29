package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction

class CanzoneTest : ActionTest() {

    private val canzone = Canzone()

    @Test
    fun testPositive() {
        canzone.doAction(message("!canzone 2 Seconds Video"))

        assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendAudio = botArguments[1] as SendAudio
        assertThat(sendChatAction.actionType).isEqualTo(ActionType.UPLOADDOCUMENT)
        assertThat(sendAudio.audio.mediaName).isEqualTo("A_Few_Moments_Later_HD_2_Seconds_Video.mp3")
    }
}