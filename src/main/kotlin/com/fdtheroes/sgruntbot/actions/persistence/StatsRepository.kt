package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface StatsRepository : CrudRepository<Stats, Long> {

    fun findStatsByUserIdAndStatDay(userId: Long, day: LocalDate): Stats?

    fun findStatsByStatDayBetween(startStatDay: LocalDate, endStatDay: LocalDate): List<Stats>

    fun findStatsByStatDayBetweenAndUserId(startStatDay: LocalDate, endStatDay: LocalDate, userId: Long): Stats?

}
