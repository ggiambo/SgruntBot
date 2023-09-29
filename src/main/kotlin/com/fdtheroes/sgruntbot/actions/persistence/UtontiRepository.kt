package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Utonto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UtontiRepository : JpaRepository<Utonto, Long> {

    fun findByUserId(userId: Long): Utonto?

}
