package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Karma
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface KarmaRepository : CrudRepository<Karma, Long> {

    fun getByUserId(userId: Long): Karma

    @Transactional
    fun deleteAllByUserIdIn(userIds: List<Long>)

    companion object {
        const val dailyKarmaCredit = 5
    }

}