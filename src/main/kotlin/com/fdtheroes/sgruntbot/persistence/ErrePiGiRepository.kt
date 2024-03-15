package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.ErrePiGi
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ErrePiGiRepository : JpaRepository<ErrePiGi, Long> {
    @Transactional
    fun deleteAllByUserIdIn(userIds: List<Long>)
}