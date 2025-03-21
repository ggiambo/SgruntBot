package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.elseIfNull
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class StatsService(private val statsRepository: StatsRepository) {

    fun increaseStats(userId: Long) {
        val stats = statsRepository.findStatsByUserIdAndStatDay(userId, LocalDate.now()) elseIfNull Stats(
            userId = userId,
            messages = 0
        )
        stats!!.messages++
        statsRepository.save(stats)
    }

    fun getStatsFromDate(startDate: LocalDate): List<Stats> {
        return statsRepository.findStatsByStatDayBetween(startDate, LocalDate.now())
    }

    fun getStatsToday(): List<Stats> {
        return statsRepository.findStatsByStatDayBetween(LocalDate.now(), LocalDate.now())
    }

    fun getStatsThisMonthByDay(): Map<Int, List<Stats>> {
        val beginOfMonth = LocalDate.now().withDayOfMonth(1)
        return statsRepository.findStatsByStatDayBetween(beginOfMonth, LocalDate.now())
            .groupBy { it.statDay.dayOfMonth }
    }

    fun getStatsThisWeek(): List<Stats> {
        val beginOfWeek = LocalDate.now().with(DayOfWeek.MONDAY)
        return aggregate(statsRepository.findStatsByStatDayBetween(beginOfWeek, LocalDate.now()))
    }

    fun getStatsThisMonth(): List<Stats> {
        val beginOfMonth = LocalDate.now().withDayOfMonth(1)
        return aggregate(statsRepository.findStatsByStatDayBetween(beginOfMonth, LocalDate.now()))
    }

    fun getStatsLastDays(days: Long): List<Stats> {
        val startStatDay = LocalDate.now().minusDays(days)
        return aggregate(statsRepository.findStatsByStatDayBetween(startStatDay, LocalDate.now()))
    }

    fun getStatsThisYear(): List<Stats> {
        val beginOfYear = LocalDate.now().withDayOfYear(1)
        return aggregate(statsRepository.findStatsByStatDayBetween(beginOfYear, LocalDate.now()))
    }

    /**
     * Aggregate (Sum of messages) by userId
     */
    private fun aggregate(stats: List<Stats>): List<Stats> {
        return stats.groupBy { it.userId }
            .map { entry ->
                val userId = entry.key
                val statsByUser = entry.value
                statsByUser.fold(Stats(userId = userId, messages = 0)) { sum, element ->
                    sum.messages += element.messages
                    sum
                }
            }
    }
}