package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Stats
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
interface StatsRepository : CrudRepository<Stats, Long> {

    fun findStatsByUserIdAndStatDay(userId: Long, day: LocalDate): Stats?

    @Transactional
    fun deleteAllByUserIdIn(userIds: List<Long>)

    fun findStatsByStatDayBetween(startStatDay: LocalDate, endStatDay: LocalDate): List<Stats>

}