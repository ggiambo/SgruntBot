package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Scommessina
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScommessinaRepository : JpaRepository<Scommessina, Int> {
    fun findScommessinaByMessageId(messageId: Int): Scommessina?
}