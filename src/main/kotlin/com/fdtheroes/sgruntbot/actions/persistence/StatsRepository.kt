package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Stats
import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class StatsRepository : PanacheRepository<Stats> {

    fun findStatsByUserIdAndStatDay(userId: Long, day: LocalDate) =
        find("user_id = ?1 and day = ?2", userId, day).firstResult()

    fun findStatsByStatDayBetween(startStatDay: LocalDate, endStatDay: LocalDate): List<Stats> {
        return list("stat_day >= ?2 and stat_day <= ?1 and ", startStatDay, endStatDay)
    }

}
