package com.fdtheroes.sgruntbot.actions

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.LocalDateTime

class CheOreSonoTest : ActionTest() {

    @Test
    fun testPositive_prima_di_mezzogiorno() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 11, 44) }
        cheOreSono.doAction(message("non so che ore sono a dire il vero"), sgruntBot)

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("mezzogiorno meno venti (precisamente 11:44 ok?)")
    }

    @Test
    fun testPositive_dopo_mezzogiorno() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 12, 17) }
        cheOreSono.doAction(message("non so che ore sono a dire il vero"), sgruntBot)

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("mezzogiorno e un quarto (precisamente 12:17 ok?)")
    }

    @Test
    fun testPositive_prima_di_mezzanotte() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 23, 44) }
        cheOreSono.doAction(message("non so che ore sono a dire il vero"), sgruntBot)

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("mezzanotte meno venti (precisamente 23:44 ok?)")
    }

    @Test
    fun testPositive_dopo_mezzanotte() {
        val cheOreSono = CheOreSono { LocalDateTime.of(2021, 11, 28, 0, 17) }
        cheOreSono.doAction(message("non so che ore sono a dire il vero"), sgruntBot)

        Assertions.assertThat(botArguments).hasSize(2)
        val sendChatAction = botArguments[0] as SendChatAction
        val sendMessage = botArguments[1] as SendMessage
        Assertions.assertThat(sendChatAction.actionType).isEqualTo(ActionType.TYPING)
        Assertions.assertThat(sendMessage.text).isEqualTo("mezzanotte e un quarto (precisamente 00:17 ok?)")
    }
}