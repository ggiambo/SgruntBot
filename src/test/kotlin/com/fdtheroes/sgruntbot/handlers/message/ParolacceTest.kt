package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ParolacceTest : BaseTest() {

    private val parolacce = Parolacce(botUtils, botConfig)

    @ParameterizedTest
    @ValueSource(strings = ["cazzone", "culona", " fica ", "stronzi", "merdah!"])
    fun testPositive(parolaccia: String) {
        botConfig.pignolo = true
        val message = message("blah banf $parolaccia yadda yadda")
        parolacce.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).contains("""<a href="tg://user?id=42">Pippo</a>""")
    }
}
