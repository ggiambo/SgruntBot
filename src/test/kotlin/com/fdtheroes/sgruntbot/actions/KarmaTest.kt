package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

internal class KarmaTest : BaseTest() {

    @Test
    fun testGetKarma() {
        val karma = Karma(botUtils, karmaService(0, 99))

        val ctx = actionContext("!karma")
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("""<b><u>Karma Report</u></b>

<pre></pre>""")
    }

    @Test
    fun testKarmaPlus_self() {
        val karma = Karma(botUtils, karmaService(0, 99))

        val replyToMessage = message("Message")
        val ctx = actionContext("+", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
    }

    @Test
    fun testKarmaPlus_noCredit() {
        val karma = Karma(botUtils, karmaService(0, 99))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("+", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Hai terminato i crediti per oggi")
    }

    @Test
    fun testKarmaPlus() {
        val karma = Karma(botUtils, karmaService(10, 99))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("+", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("""Karma totale di <a href="tg://user?id=252800958">DA_DA212</a>: 99
Crediti di <a href="tg://user?id=42">Pippo</a>: 10""")
    }

    @Test
    fun testKarmaMinus_self() {
        val karma = Karma(botUtils, karmaService(0, 99))

        val replyToMessage = message("Message")
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
    }


    @Test
    fun testKarmaMinus_noCredit() {
        val karma = Karma(botUtils, karmaService(0, 99))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Hai terminato i crediti per oggi")
    }


    @Test
    fun testKarmaMinus() {
        val karma = Karma(botUtils, karmaService(10, 99))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith("""Karma totale di <a href="tg://user?id=252800958">DA_DA212</a>: 99
Crediti di <a href="tg://user?id=42">Pippo</a>: 10""")
    }

    private fun karmaService(credits: Int, karma: Int) : KarmaService {
        return mock {
            on { getKarmaCredit(isA())} doReturn credits
            on { getKarma(isA())} doReturn karma
        }
    }
}
