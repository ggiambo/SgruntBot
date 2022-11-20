package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ComplimentoRepository : CrudRepository<Complimento, Long> {

    @Modifying
    @Query("insert into sgrunt.complimenti (user_id, complimento) values (:userId, :complimento)")
    fun createComplimento(userId: Long, complimento: String)
}