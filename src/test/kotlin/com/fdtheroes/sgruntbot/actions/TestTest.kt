package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TestTest : BaseTest() {

    private val test = com.fdtheroes.sgruntbot.handlers.message.Test(botUtils, botConfig)

    @Test
    fun testPositive() {
        val message = message("!test")
        test.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("""<a href="tg://user?id=42">Pippo</a>: toast <pre>test</pre>""")
    }

}