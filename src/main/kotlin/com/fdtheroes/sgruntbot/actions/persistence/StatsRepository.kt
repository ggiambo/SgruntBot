package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface StatsRepository : CrudRepository<Stats, Long> {

    fun findStatsByUserIdAndStatDay(userId: Long, day: LocalDate): Stats?

    fun findStatsByStatDayBetween(startStatDay: LocalDate, endStatDay: LocalDate): List<Stats>

    @Query("select distinct user_id from sgrunt.stats")
    fun allIds() : List<Long>

}