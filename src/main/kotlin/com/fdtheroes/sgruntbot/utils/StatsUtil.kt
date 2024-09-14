package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.persistence.StatsService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.text.DecimalFormat

@Service
class StatsUtil(
    private val statsService: StatsService,
    private val botUtils: BotUtils,
) {

    private val percentageFormatter = DecimalFormat("00.00")

    fun getStats(tipo: StatsType): InputFile {
        val stats = when (tipo) {
            StatsType.GIORNO -> statsService.getStatsToday()
            StatsType.SETTIMANA -> statsService.getStatsThisWeek()
            StatsType.MESE -> statsService.getStatsThisMonth()
            StatsType.ANNO -> statsService.getStatsThisYear()
        }
        return getStatsInputFile(stats, "Logorroici di ${tipo.desc}")
    }

    fun getStats(days: Long): InputFile {
        val stats = statsService.getStatsLastDays(days)
        return getStatsInputFile(stats, "Logorroici degli ultimi $days giorni")
    }

    private fun getStatsInputFile(
        stats: List<Stats>,
        chartTitle: String,
    ): InputFile {
        val totalMessages = stats.sumOf { it.messages }
        val pieChart = ChartUtils.pieChart(chartTitle)
        pieChart.seriesMap.clear()
        stats
            .sortedBy { it.messages }
            .asReversed()
            .forEach { stat ->
                val userName = botUtils.getUserName(botUtils.getChatMember(stat.userId))
                val percentage = getPercentage(stat.messages, totalMessages)
                val formattedPercentage = percentageFormatter.format(percentage)
                val name = "$userName $formattedPercentage%"
                pieChart.addSeries(name, stat.messages)
            }


        return ChartUtils.getAsInputFile(pieChart)
    }

    private fun getPercentage(messages: Int, total: Int): Double {
        return (messages * 100) / total.toDouble()
    }

    enum class StatsType(val type: String, val desc: String) {
        GIORNO("g", "oggi"),
        SETTIMANA("s", "questa settimana"),
        MESE("m", "questo mese"),
        ANNO("a", "tutto l'anno")
        ;

        companion object {
            private val byType = entries.associateBy { it.type }
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
