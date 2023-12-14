package com.fdtheroes.sgruntbot.utils;

import com.fdtheroes.sgruntbot.actions.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.User

@Service
class StatsUtil(
    private val statsService: StatsService,
    private val botUtils: BotUtils,
) {

    fun getStats(tipo: StatsType, getChatMember: (Long) -> User?): InputFile {
        val stats = when (tipo) {
            StatsType.GIORNO -> statsService.getStatsToday()
            StatsType.SETTIMANA -> statsService.getStatsThisWeek()
            StatsType.MESE -> statsService.getStatsThisMonth()
            StatsType.ANNO -> statsService.getStatsThisYear()
        }
        return getStatsInputFile(stats, "Logorroici di ${tipo.desc}", getChatMember)
    }

    fun getStats(days: Long, getChatMember: (Long) -> User?): InputFile {
        val stats = statsService.getStatsLastDays(days)
        return getStatsInputFile(stats, "Logorroici degli ultimi $stats giorni", getChatMember)
    }

    private fun getStatsInputFile(
        stats: List<com.fdtheroes.sgruntbot.actions.models.Stats>,
        chartTitle: String,
        getChatMember: (Long) -> User?
    ): InputFile {
        val totalMessages = stats.sumOf { it.messages }
        val pieChart = ChartUtils.pieChart(chartTitle)
        pieChart.seriesMap.clear()
        stats
            .sortedBy { it.messages }
            .asReversed()
            .forEach { stat ->
                val userName = botUtils.getUserName(getChatMember(stat.userId))
                val percentage = getPercentage(stat.messages, totalMessages)
                val name = "$userName $percentage%"
                pieChart.addSeries(name, stat.messages)
            }


        return ChartUtils.getAsInputFile(pieChart)
    }

    private fun getPercentage(messages: Int, total: Int): Int {
        return (messages * 100) / total
    }

    enum class StatsType(val type: String, val desc: String) {
        GIORNO("g", "oggi"),
        SETTIMANA("s", "questa settimana"),
        MESE("m", "questo mese"),
        ANNO("a", "tutto l'anno")
        ;

        companion object {
            private val byType = StatsType.values().associateBy { it.type }
            fun getByType(type: String?): StatsType {
                val tipo = type.orEmpty().trim().lowercase()
                if (tipo.isEmpty()) {
                    return GIORNO
                }
                return byType.getValue(tipo)
            }
        }
    }

}
