package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Utonto
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UtontiRepository : CrudRepository<Utonto, Long> {
    fun findByUserId(userId: Long): Utonto?

    fun getByUserId(userId: Long): Utonto

    @Modifying
    @Query("insert into sgrunt.utonti (user_id, first_name, last_name, user_name, updated) values (:userId, :firstName, :lastName, :userName, :updated)")
    fun createUtonto(
        userId: Long,
        firstName: String,
        lastName: String?,
        userName: String?,
        updated: LocalDateTime = LocalDateTime.now()
    )

    @Modifying
    @Query("update sgrunt.utonti set sgrunt.utonti.first_name = :firstName, sgrunt.utonti.last_name = :lastName, sgrunt.utonti.user_name = :userName where sgrunt.utonti.user_id = :userId")
    fun updateUtonto(
        userId: Long,
        firstName: String?,
        lastName: String?,
        userName: String?,
        updated: LocalDateTime = LocalDateTime.now()
    )

}
