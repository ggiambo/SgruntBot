package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Scommessina
import com.fdtheroes.sgruntbot.persistence.ScommessinaRepository
import com.fdtheroes.sgruntbot.persistence.ScommessinaService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate

internal class ScommessinaTest : BaseTest() {

    private val message = message("!scommessina domani sorge il sole").apply {
        messageId = 1111
    }

    private val scommessinaRepository = mock<ScommessinaRepository> {
        on { save(isA<Scommessina>()) } doAnswer {
            it.arguments.first() as Scommessina
        }
    }
    private val scommessina = Scommessina(botUtils, botConfig, ScommessinaService(scommessinaRepository))

    @Test
    fun handle_scommessina() {
        scommessina.handle(message)

        argumentCaptor<Scommessina>().apply {
            verify(scommessinaRepository, times(1)).save(capture())
            assertThat(firstValue.userId).isEqualTo(42)
            assertThat(firstValue.content).isEqualTo("domani sorge il sole")
            assertThat(firstValue.created).isEqualTo(LocalDate.now())
            assertThat(firstValue.messageId).isEqualTo(1111)
            assertThat(firstValue.partecipantsUserId).isEmpty()
        }

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().message).isEqualTo(
            "\uD83D\uDE80 Scommessina creata!\nPer partecipare rispondi con <b>!scommessina</b> al messaggio originale."
        )
    }

    @Test
    @DisplayName("Risponde al messaggio sbagliato")
    fun handle_accetta_rispostaSbagliata() {
        val messageAccetta = message("!scommessina").apply {
            messageId = 2222
            replyToMessage = message
        }

        scommessina.handle(messageAccetta)

        argumentCaptor<Int>().apply {
            verify(scommessinaRepository, times(1)).findScommessinaByMessageId(capture())
            assertThat(firstValue).isEqualTo(1111)
        }
        verify(scommessinaRepository, times(0)).save(any())
        assertThat(actionResponses.size).isEqualTo(1)
        assertThat(actionResponses.first().message).isEqualTo("Devi rispondere al messaggio originale con i termini della scommessa se vuoi accettare \uD83D\uDE44")
    }

    @Test
    @DisplayName("Accetta la scommesa che ha creato")
    fun handle_accetta_creatore() {
        val messageAccetta = message("!scommessina").apply {
            from = message.from
            messageId = 2222
            replyToMessage = message
        }
        whenever { scommessinaRepository.findScommessinaByMessageId(message.messageId) } doAnswer {
            Scommessina(userId = 42, content = "domani sorge il sole", messageId = message.messageId)
        }

        scommessina.handle(messageAccetta)

        argumentCaptor<Int>().apply {
            verify(scommessinaRepository, times(1)).findScommessinaByMessageId(capture())
            assertThat(firstValue).isEqualTo(1111)
        }
        verify(scommessinaRepository, times(0)).save(any())
        assertThat(actionResponses.size).isEqualTo(1)
        assertThat(actionResponses.first().message).isEqualTo("Hai creato tu questa scommessa, incapace \uD83D\uDE21!")
    }

    @Test
    @DisplayName("Accetta la scommesa che ha già accettato")
    fun handle_accetta_gia_accettato() {
        val messageAccetta = message("!scommessina").apply {
            from = user(99)
            messageId = 2222
            replyToMessage = message
        }
        whenever { scommessinaRepository.findScommessinaByMessageId(message.messageId) } doAnswer {
            Scommessina(
                userId = 42,
                content = "domani sorge il sole",
                messageId = message.messageId,
                partecipantsUserId = listOf(99)
            )
        }

        scommessina.handle(messageAccetta)

        argumentCaptor<Int>().apply {
            verify(scommessinaRepository, times(1)).findScommessinaByMessageId(capture())
            assertThat(firstValue).isEqualTo(1111)
        }
        verify(scommessinaRepository, times(0)).save(any())
        assertThat(actionResponses.size).isEqualTo(1)
        assertThat(actionResponses.first().message).isEqualTo("Hai già accettato, vuoi farti del male due volte \uD83D\uDE16?")
    }

    @Test
    fun handle_accetta() {
        val messageAccetta = message("!scommessina").apply {
            from = user(99)
            messageId = 2222
            replyToMessage = message
        }
        whenever { scommessinaRepository.findScommessinaByMessageId(message.messageId) } doAnswer {
            Scommessina(userId = 42, content = "domani sorge il sole", messageId = message.messageId)
        }

        scommessina.handle(messageAccetta)

        argumentCaptor<Int>().apply {
            verify(scommessinaRepository, times(1)).findScommessinaByMessageId(capture())
            assertThat(firstValue).isEqualTo(1111)
        }

        argumentCaptor<Scommessina>().apply {
            verify(scommessinaRepository, times(1)).save(capture())
            assertThat(firstValue.userId).isEqualTo(42)
            assertThat(firstValue.content).isEqualTo("domani sorge il sole")
            assertThat(firstValue.created).isEqualTo(LocalDate.now())
            assertThat(firstValue.messageId).isEqualTo(1111)
            assertThat(firstValue.partecipantsUserId).containsOnly(99)
        }
    }

    @Test
    @DisplayName("Nessuna scommessa aperta")
    fun handle_lista_nessuna() {
        val messageLista = message("!scommessina").apply {
            from = user(42)
            messageId = 2222
        }

        scommessina.handle(messageLista)

        argumentCaptor<Long>().apply {
            verify(scommessinaRepository, times(1)).findAllByUserId(capture())
            assertThat(firstValue).isEqualTo(42)
        }
        assertThat(actionResponses.size).isEqualTo(1)
        assertThat(actionResponses.first().message).isEqualTo("Non hai scommesse aperte.")
    }

    @Test
    @DisplayName("Lista scommesse aperte")
    fun handle_lista() {
        val messageLista = message("!scommessina").apply {
            from = user(42)
            messageId = 2222
        }
        whenever { scommessinaRepository.findAllByUserId(any()) } doAnswer {
            listOf(Scommessina(userId = 42, content = "domani sorge il sole", messageId = message.messageId))
        }

        scommessina.handle(messageLista)

        argumentCaptor<Long>().apply {
            verify(scommessinaRepository, times(1)).findAllByUserId(capture())
            assertThat(firstValue).isEqualTo(42)
        }
        assertThat(actionResponses.size).isEqualTo(1)
        assertThat(actionResponses.first().message).isEqualTo(
            "<b>Tue scommesse aperte</b>\n" +
                    "- <a href='https://t.me/c/9999/1111'><i>domani sorge il sole</i></a>\n" +
                    "Partecipanti: Nessuno"
        )
    }

}