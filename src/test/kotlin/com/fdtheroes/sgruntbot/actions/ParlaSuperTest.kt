package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class ParlaSuperTest : BaseTest() {

    private val parlaSuper = ParlaSuper(botConfig)

    @Test
    fun testPositive() {
        parlaSuper.doAction(
            actionContext(
                text = "!parlaSuper questo bot è stupendo!",
                from = user(id = Users.AVVE.id)
            )
        )

        assertThat(botArguments).hasSize(1)
        val sendMessage = botArguments[0] as SendMessage
        assertThat(sendMessage.text).isEqualTo("questo bot è stupendo!")
    }

    @Test
    fun testNegative() {
        parlaSuper.doAction(actionContext("!parlaSuper questo bot è stupendo!"))

        assertThat(botArguments).isEmpty()
    }

}