package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Scommessina
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.time.LocalDate

@Service
class ScommessinaService(private val scommessinaRepository: ScommessinaRepository) {

    fun createScommessina(message: Message, termini: String): Scommessina {
        return scommessinaRepository.save(
            Scommessina(
                userId = message.from.id,
                content = termini,
                messageId = message.messageId,
            )
        )
    }

    fun addPartecipant(message: Message, rispondi: (String) -> Unit) {
        val scommessina = scommessinaRepository.findScommessinaByMessageId(message.replyToMessage.messageId)
        if (scommessina == null) {
            rispondi("Devi rispondere al messaggio originale con i termini della scommessa se vuoi accettare 🙄")
            return
        }

        if (scommessina.userId == message.from.id) {
            rispondi("Hai creato tu questa scommessa, incapace 😡!")
            return
        }

        val participantsUserId = scommessina.participantsUserId.toMutableList()
        if (participantsUserId.contains(message.from.id)) {
            rispondi("Hai già accettato, vuoi farti del male due volte 😖?")
            return
        }

        participantsUserId.add(message.from.id)
        scommessina.participantsUserId = participantsUserId
        scommessinaRepository.save(scommessina)

        rispondi("Hai accettato la scommessa, mò son cazzi tuoi!")
    }

    fun getByUserId(userId: Long): List<Scommessina> {
        return scommessinaRepository.findAllByUserId(userId)
    }

    fun getNoParticipantsAndWillExpireInThreeDays(): List<Scommessina> {
        val dueSettimaneFa = LocalDate.now().minusDays(14)
        return scommessinaRepository
            .findAllByCreatedBetweenAndParticipantsUserIdIsEmpty(dueSettimaneFa, dueSettimaneFa.plusDays(3))
    }

    fun getNoParticipantsAndExpired(): List<Scommessina> {
        val dueSettimaneFa = LocalDate.now().minusDays(14)
        return scommessinaRepository.findAllByCreatedBeforeAndParticipantsUserIdIsEmpty(dueSettimaneFa)
    }

    fun deleteById(scommessinaId: Long) = scommessinaRepository.deleteById(scommessinaId)
}