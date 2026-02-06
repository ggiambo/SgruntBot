package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Scommessina
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

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

        val partecipantsUserId = scommessina.partecipantsUserId.toMutableList()
        if (partecipantsUserId.contains(message.from.id)) {
            rispondi("Hai già accettato, vuoi farti del male due volte 😖?")
            return
        }

        partecipantsUserId.add(message.from.id)
        scommessina.partecipantsUserId = partecipantsUserId
        scommessinaRepository.save(scommessina)

        rispondi("Hai accettato la scommessa, mò son cazzi tuoi!")
    }
}