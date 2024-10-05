package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.handlers.message.Wiki
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class WikiTest : BaseTest() {

    private val wiki = Wiki(botUtils, botConfig, mapper)

    @Test
    fun testPositive() {
        val message = message("!wiki pOeSiA gIaMbIcA")
        wiki.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message)
            .startsWith("La poesia giambica era un tipo di poesia ")
            .endsWith("https://it.wikipedia.org/w/index.php?title=Poesia giambica")
    }

    @Test
    fun testPositive_accento() {
        val message = message("!wiki fosse ardenne")
        wiki.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message)
            .startsWith("Le Ardenne (AFI: /arˈdɛnne/; in olandese, tedesco e lussemburghese Ardennen")
            .endsWith("https://it.wikipedia.org/w/index.php?title=Ardenne")
    }

    @Test
    fun testNegative() {
        val message = message("!wiki barzuffo")
        wiki.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Non c'è.")
    }

}
