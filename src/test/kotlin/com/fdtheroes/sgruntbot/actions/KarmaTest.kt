package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import com.fdtheroes.sgruntbot.actions.models.Utonto
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

internal class KarmaTest : BaseTest() {

    @Test
    fun testGetKarma() {
        val karma = Karma(botUtils,karmaService(), usersService(listOf()))

        val ctx = actionContext("!karma")
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo(
            """<b><u>Karma Report</u></b>

<pre></pre>"""
        )
    }

    @Test
    fun testKarmaPlus_self() {
        val karma = Karma(botUtils,karmaService(), usersService(listOf()))

        val replyToMessage = message("Message")
        val ctx = actionContext("+", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
    }

    @Test
    fun testKarmaPlus_noCredit() {
        val karma = Karma(botUtils,karmaService(), usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("+", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).isEqualTo("Hai terminato i crediti per oggi")
    }

    @Test
    fun testKarmaPlus() {
        val karma = Karma(botUtils, karmaService(), usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("+", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses.first().message).startsWith(
            """Karma totale di <a href="tg://user?id=252800958">DA_DA212</a>: 101
Crediti di <a href="tg://user?id=42">Pippo</a>: 10"""
        )
    }

    @Test
    fun testKarmaMinus_self() {
        val karma = Karma(botUtils,karmaService(), usersService(listOf()))

        val replyToMessage = message("Message")
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        val karmaMessage = ctx.actionResponses[0]
        assertThat(karmaMessage.type).isEqualTo(ActionResponseType.Message)
        assertThat(karmaMessage.message).isEqualTo("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
    }


    @Test
    fun testKarmaMinus_noCredit() {
        val karma = Karma(botUtils,karmaService(), usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        val karmaMessage = ctx.actionResponses[0]
        assertThat(karmaMessage.type).isEqualTo(ActionResponseType.Message)
        assertThat(karmaMessage.message).isEqualTo("Hai terminato i crediti per oggi")
    }


    @Test
    fun testKarmaMinus() {
        val karma = Karma(botUtils, karmaService(), usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA212))
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        val karmaMessage = ctx.actionResponses[0]
        assertThat(karmaMessage.type).isEqualTo(ActionResponseType.Message)
        assertThat(karmaMessage.message).startsWith(
            """Karma totale di <a href="tg://user?id=252800958">DA_DA212</a>: 99
Crediti di <a href="tg://user?id=42">Pippo</a>: 10"""
        )
    }

    @Test
    fun testKarmaToBot() {
        val bot = Utonto(
            firstName = "SgruntBot",
            lastName = null,
            userName = "BlahBanfBot",
            isBot = true,
            updated = LocalDate.of(2023, 1, 1),
            userId = Users.BLAHBANFBOT.id,
        )
        val karma = Karma(botUtils, karmaService(), usersService(listOf(bot)))

        val replyToMessage = message("Message", user(Users.BLAHBANFBOT))
        val ctx = actionContext("-", replyToMessage = replyToMessage)
        karma.doAction(ctx)

        assertThat(ctx.actionResponses).hasSize(1)
        assertThat(ctx.actionResponses[0].type).isEqualTo(ActionResponseType.Message)
        assertThat(ctx.actionResponses[0].message).isEqualTo("SgruntBot è un bot senz'anima. Assegna il karma saggiamente")
    }

    private fun karmaService(): KarmaService {
        val karma1 = com.fdtheroes.sgruntbot.actions.models.Karma(
            karma = 100,
            karmaCredit = 1,
            userId = Users.DA_DA212.id
        )
        val karma2 = com.fdtheroes.sgruntbot.actions.models.Karma(
            karma = 2,
            karmaCredit = 11,
            userId = 42
        )
        val karmaRepository = mock<KarmaRepository> {
            on { getByUserId(eq(Users.DA_DA212.id)) } doReturn karma1
            on { getByUserId(eq(42)) } doReturn karma2
            on  { findAll() } doReturn listOf(karma1, karma2)
        }
        return KarmaService(botUtils, karmaRepository)
    }

    private fun usersService(utonti: List<Utonto>): UsersService {
        return mock<UsersService> {
            on { getUser(isA()) } doAnswer { params ->
                val userId = params.arguments.first() as Long
                utonti.firstOrNull { it.userId == userId }
            }
        }
    }
}
