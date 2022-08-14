package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BotUtils.Companion.elseIfNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Month

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

    fun getStatsToday(): List<Stats> {
        return statsRepository.findStatsByStatDayBetween(LocalDate.now(), LocalDate.now())
    }

    fun getStatsThisMonthByDay(): Map<Int, List<Stats>> {
        val beginOfMonth = LocalDate.now().withDayOfMonth(1)
        return statsRepository.findStatsByStatDayBetween(beginOfMonth, LocalDate.now())
            .groupBy { it.statDay.dayOfMonth }
    }

    fun getStatsThisMonth(): List<Stats> {
        val beginOfMonth = LocalDate.now().withDayOfMonth(1)
        return aggregate(statsRepository.findStatsByStatDayBetween(beginOfMonth, LocalDate.now()))
    }

    fun getStatsThisYearByMonth(): Map<Month, List<Stats>> {
        val beginOfYear = LocalDate.now().withDayOfYear(1)
        return statsRepository.findStatsByStatDayBetween(beginOfYear, LocalDate.now())
            .groupBy { it.statDay.month }
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
                    sum.messages = sum.messages + element.messages
                    sum
                }
            }
    }
}