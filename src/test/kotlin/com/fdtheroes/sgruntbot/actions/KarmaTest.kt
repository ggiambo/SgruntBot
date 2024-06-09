package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.handlers.message.Karma
import com.fdtheroes.sgruntbot.models.ActionResponseType
import com.fdtheroes.sgruntbot.models.Utonto
import com.fdtheroes.sgruntbot.persistence.KarmaRepository
import com.fdtheroes.sgruntbot.persistence.KarmaService
import com.fdtheroes.sgruntbot.persistence.UsersService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate
import java.util.*

internal class KarmaTest : BaseTest() {

    @Test
    fun testGetKarma() {
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf()))

        val message = message("!karma")
        karma.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo(
            """
            <b><u>Karma Report</u></b>

            <pre>Username_252800958  100
            Username_42           2
            Username_99           2</pre>""".trimIndent()
        )
    }

    @Test
    fun testKarmaPlus_self() {
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf()))

        val replyToMessage = message("Message")
        val message = message("+", replyToMessage = replyToMessage)
        karma.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses.first().message).isEqualTo("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
    }

    @Test
    fun testKarmaPlus_noCredit() {
        val donatore = user(99, "Donatore")
        val donatoreUtonto = Utonto(firstName = donatore.userName, null, null, false, userId = donatore.id)
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf(donatoreUtonto)))
        val replyToMessage = message("Message", user(Users.DA_DA_212))
        val message = message("+", donatore, replyToMessage = replyToMessage)

        karma.handle(message)

        assertThat(actionResponses).hasSize(1)
        val karmaMessage = actionResponses[0]
        assertThat(karmaMessage.type).isEqualTo(ActionResponseType.Message)
        assertThat(karmaMessage.message).isEqualTo("Hai terminato i crediti per oggi")
    }

    @Test
    fun testKarmaPlus() {
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA_212))
        karma.handle(message("+", replyToMessage = replyToMessage))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        val message = actionResponses.first().message
        assertThat(message).startsWith("Karma totale di <a href=\"tg://user?id=252800958\">DA_DA_212</a>: 101")
        if (message.contains("Karmaroulette")) {
            assertThat(message).contains("<b>Karmaroulette</b> ! Hai vinto 1 karma, e ora sei a quota 3")
        }
        if (message.contains("Creditroulette")) {
            assertThat(message).contains("<b>Creditroulette</b> ! Hai vinto un credito")
            assertThat(message).contains("Crediti di <a href=\"tg://user?id=42\">Pippo</a>: 11")
        } else {
            assertThat(message).contains("Crediti di <a href=\"tg://user?id=42\">Pippo</a>: 10")
        }
    }

    @Test
    fun testKarmaMinus_self() {
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf()))

        val replyToMessage = message("Message")
        val message = message("-", replyToMessage = replyToMessage)
        karma.handle(message)

        assertThat(actionResponses).hasSize(1)
        val karmaMessage = actionResponses[0]
        assertThat(karmaMessage.type).isEqualTo(ActionResponseType.Message)
        assertThat(karmaMessage.message).isEqualTo("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
    }


    @Test
    fun testKarmaMinus_noCredit() {
        val donatore = user(99, "Donatore")
        val donatoreUtonto = Utonto(firstName = donatore.userName, null, null, false, userId = donatore.id)
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf(donatoreUtonto)))
        val replyToMessage = message("Message", user(Users.DA_DA_212))
        val message = message("-", donatore, replyToMessage = replyToMessage)

        karma.handle(message)

        assertThat(actionResponses).hasSize(1)
        val karmaMessage = actionResponses[0]
        assertThat(karmaMessage.type).isEqualTo(ActionResponseType.Message)
        assertThat(karmaMessage.message).isEqualTo("Hai terminato i crediti per oggi")
    }


    @Test
    fun testKarmaMinus() {
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA_212))
        karma.handle(message("-", replyToMessage = replyToMessage))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        val message = actionResponses.first().message
        assertThat(message).startsWith("Karma totale di <a href=\"tg://user?id=252800958\">DA_DA_212</a>: 99")
        if (message.contains("Karmaroulette")) {
            assertThat(message).contains("<b>Karmaroulette</b> ! Hai perso 1 karma, e ora sei a quota 1")
        }
        if (message.contains("Creditroulette")) {
            assertThat(message).contains("<b>Creditroulette</b> ! Hai vinto un credito")
            assertThat(message).contains("Crediti di <a href=\"tg://user?id=42\">Pippo</a>: 11")
        } else {
            assertThat(message).contains("Crediti di <a href=\"tg://user?id=42\">Pippo</a>: 10")
        }
    }

    @Test
    fun testKarmaToBot() {
        val bot = Utonto(
            firstName = "SgruntBot",
            lastName = null,
            userName = "BlahBanfBot",
            isBot = true,
            updated = LocalDate.of(2023, 1, 1),
            userId = Users.BLAH_BANF_BOT.id,
        )
        val karma = Karma(botUtils, botConfig, karmaService(), usersService(listOf(bot)))

        val replyToMessage = message("Message", user(Users.BLAH_BANF_BOT))
        val message = message("-", replyToMessage = replyToMessage)
        karma.handle(message)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses[0].type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponses[0].message).isEqualTo("SgruntBot è un bot senz'anima. Assegna il karma saggiamente")
    }

    @Test
    fun testKarmaTooManyPlus() {
        val karmaRepository = karmaRepository()
        val karmaService = spy(KarmaService(botUtils, karmaRepository))
        val karma = Karma(botUtils, botConfig, karmaService, usersService(listOf()))

        val replyToMessage = message("Message", user(Users.DA_DA_212))
        karma.handle(message("+++++++++++++++", replyToMessage = replyToMessage))

        // User has 11 credits but gives 15 "+"
        val argumentCaptor = argumentCaptor<Long, Long, (Int) -> Int>()
        verify(karmaService, times(11)).takeGiveKarma(
            argumentCaptor.first.capture(),
            argumentCaptor.second.capture(),
            argumentCaptor.third.capture()
        )
        assertThat(argumentCaptor.first.allValues).containsOnly(42L)
        assertThat(argumentCaptor.second.allValues).containsOnly(252800958L)

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().type).isEqualTo(ActionResponseType.Message)
        val message = actionResponses.first().message
        assertThat(message).startsWith("Karma totale di <a href=\"tg://user?id=252800958\">DA_DA_212</a>: 111")
        if (message.contains("Karmaroulette")) {
            assertThat(message).contains("<b>Karmaroulette</b> ! Hai vinto ")
            assertThat(message).contains(", e ora sei a quota")
        }
        if (message.contains("Creditroulette")) {
            assertThat(message).contains("<b>Creditroulette</b> ! Hai vinto")
            assertThat(message).contains("Crediti di <a href=\"tg://user?id=42\">Pippo</a>: ")
        } else {
            assertThat(message).contains("Crediti di <a href=\"tg://user?id=42\">Pippo</a>: ")
        }
    }

    private fun karmaService(): KarmaService {
        return KarmaService(botUtils, karmaRepository())
    }

    private fun karmaRepository(): KarmaRepository {
        val karmas = listOf(
            com.fdtheroes.sgruntbot.models.Karma(
                karma = 100,
                karmaCredit = 1,
                userId = Users.DA_DA_212.id
            ),
            com.fdtheroes.sgruntbot.models.Karma(
                karma = 2,
                karmaCredit = 11,
                userId = 42
            ),
            com.fdtheroes.sgruntbot.models.Karma(
                karma = 2,
                karmaCredit = 0,
                userId = 99
            )
        )
        return mock<KarmaRepository> {
            on { getByUserId(isA()) } doAnswer { args ->
                karmas.firstOrNull { it.userId == args.arguments.first() }
            }
            on { findById(isA()) } doAnswer { args ->
                Optional.ofNullable(karmas.firstOrNull { it.userId == args.arguments.first() })
            }
            on { findAll() } doReturn karmas
            onGeneric { save(isA()) } doAnswer { args ->
                args.arguments.firstOrNull() as com.fdtheroes.sgruntbot.models.Karma
            }
        }
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
