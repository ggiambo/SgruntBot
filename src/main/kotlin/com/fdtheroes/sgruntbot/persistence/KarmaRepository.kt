package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Karma
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface KarmaRepository : CrudRepository<Karma, Long> {

    fun getByUserId(userId: Long): Karma

    @Transactional
    fun deleteAllByUserIdNotIn(userIds: List<Long>): List<Karma>

    companion object {
        const val dailyKarmaCredit = 5
    }

}