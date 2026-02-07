package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Scommessina
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ScommessinaRepository : JpaRepository<Scommessina, Long> {
    fun findScommessinaByMessageId(messageId: Int): Scommessina?
    fun findAllByUserId(userId: Long): List<Scommessina>
    fun findAllByCreatedBeforeAndParticipantsUserIdIsEmpty(date: LocalDate): List<Scommessina>
    fun findAllByCreatedBetweenAndParticipantsUserIdIsEmpty(fromDate: LocalDate, toDate: LocalDate): List<Scommessina>
}