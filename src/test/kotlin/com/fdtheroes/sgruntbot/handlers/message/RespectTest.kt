package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class RespectTest : BaseTest() {

    private val respect = Respect(botUtils, botConfig)

    @Test
    fun testPositive() {
        val message = message(
            text = "F",
            replyToMessage = message(text = "whatever", from = user(id = Users.IL_VINCI.id, userName = "AvveFaTutti"))
        )
        respect.handle(message)

        Assertions.assertThat(actionResponses).hasSize(1)
        Assertions.assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        Assertions.assertThat(actionResponses.first().message)
            .isEqualTo("""Baciamo le mani Don <a href="tg://user?id=104278889">AvveFaTutti</a>""")
    }

    @Test
    fun testNegative() {
        val message = message("F..anculo")
        respect.handle(message)

        Assertions.assertThat(actionResponses).isEmpty()
    }
}