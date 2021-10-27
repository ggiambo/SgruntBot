package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class ParlaSuperTest : ActionTest() {

    private val parlaSuper = ParlaSuper()

    @Test
    fun testPositive() {
        parlaSuper.doAction(
            message(
                text = "!parlaSuper questo bot è stupendo!",
                from = user(id = BotUtils.Users.AVVE.id)
            ), Context()
        )

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("questo bot è stupendo!")
    }

    @Test
    fun testNegative() {
        parlaSuper.doAction(message("!parlaSuper questo bot è stupendo!"), Context())

        assertThat(botArguments).isEmpty()
    }

}