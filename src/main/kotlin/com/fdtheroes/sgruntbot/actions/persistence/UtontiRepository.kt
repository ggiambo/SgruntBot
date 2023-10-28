package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Utonto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UtontiRepository : JpaRepository<Utonto, Long> {

    fun findByUserId(userId: Long): Utonto?

    @Transactional
    fun deleteAllByUserIdIn(userIds: List<Long>)

}
