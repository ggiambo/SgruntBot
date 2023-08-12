package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class ParolacceTest : BaseTest() {

    private val parolacce = Parolacce(botUtils, botConfig)

    @ParameterizedTest
    @ValueSource(strings = ["cazzone", "culona", " fica ", "stronzi", "merdah!"])
    fun testPositive(parolaccia: String) {
        botConfig.pignolo = true
        val ctx = actionContext("blah banf $parolaccia yadda yadda")
        parolacce.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).contains("""<a href="tg://user?id=42">Pippo</a>""")
    }
}
