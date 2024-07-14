package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Utonto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UtontiRepository : JpaRepository<Utonto, Long> {

    fun findByUserId(userId: Long): Utonto?

    @Transactional
    fun deleteAllByUserIdNotIn(userIds: List<Long>): List<Utonto>

}
