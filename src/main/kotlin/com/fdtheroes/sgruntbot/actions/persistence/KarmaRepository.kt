package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Karma
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KarmaRepository : CrudRepository<Karma, Long> {

    fun getByUserId(userId: Long): Karma

    companion object {
        const val dailyKarmaCredit = 5
    }

}