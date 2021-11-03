package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto

class TappetoTest : ActionTest() {

    private val tappeto = Tappeto()

    @Test
    fun testPositive() {
        tappeto.doAction(message("!tappeto malattia e tutto il FdT"))

        Assertions.assertThat(botArguments).hasSize(1)
        val sendPhoto = botArguments[0] as SendPhoto
        Assertions.assertThat(sendPhoto.chatId).isEqualTo(BotUtils.chatId)
        Assertions.assertThat(sendPhoto.photo.mediaName).isEqualTo("tappeto.jpg")
        Assertions.assertThat(sendPhoto.caption).isEqualTo("Pippo manda malattia e tutto il FdT al tappeto!")
    }
}