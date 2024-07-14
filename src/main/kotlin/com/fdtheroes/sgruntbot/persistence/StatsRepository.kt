package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Stats
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
interface StatsRepository : CrudRepository<Stats, Long> {

    fun findStatsByUserIdAndStatDay(userId: Long, day: LocalDate): Stats?

    @Transactional
    fun deleteAllByUserIdNotIn(userIds: List<Long>): List<Stats>

    fun findStatsByStatDayBetween(startStatDay: LocalDate, endStatDay: LocalDate): List<Stats>

}