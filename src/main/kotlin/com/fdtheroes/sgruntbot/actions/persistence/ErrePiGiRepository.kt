package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.ErrePiGi
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ErrePiGiRepository : CrudRepository<ErrePiGi, Long> {
    fun getErrePiGiByUserId(userId: Long): ErrePiGi?

    @Modifying
    @Query("insert into sgrunt.errepigi (user_id, hp, attaccanti_ids) values (:#{#errePiGi.userId}, :#{#errePiGi.hp}, :#{#errePiGi.attaccantiIds})")
    fun createErrePiGi(errePiGi: ErrePiGi)

}