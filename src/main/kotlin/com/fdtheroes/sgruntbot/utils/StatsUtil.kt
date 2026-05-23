package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.persistence.StatsService
import org.jfree.chart.ChartUtils
import org.jfree.chart.JFreeChart
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDate

@Service
class StatsUtil(
    private val statsService: StatsService,
    private val pieChartUtils: PieChartUtils,
    private val xyChartUtils: XYChartUtils,
) {

    fun getStats(tipo: StatsType): InputFile {
        val stats = when (tipo) {
            StatsType.GIORNO -> statsService.getStatsToday()
            StatsType.SETTIMANA -> statsService.getStatsThisWeek()
            StatsType.MESE -> statsService.getStatsThisMonth()
            StatsType.ANNO -> statsService.getStatsThisYear()
        }
        val chart = pieChartUtils.pieChart(stats, "Logorroici di ${tipo.desc}")
        return chartToInputFile(chart)
    }

    fun getWeeklyEvolution(): InputFile {
        val sevenDaysAgo = LocalDate.now().minusDays(6)
        val stats = statsService.getStatsFromDate(sevenDaysAgo)
            .groupBy { it.statDay }
            .mapValues { (statDay, stat) ->
                stat.fold(Stats(userId = -1, statDay = statDay, messages = 0)) { sum, element ->
                    sum.messages += element.messages
                    sum
                }
            }.values.toList()

        val chart = xyChartUtils.xyChart(stats)
        return chartToInputFile(chart)
    }

    private fun chartToInputFile(chart: JFreeChart): InputFile {
        return ByteArrayOutputStream().use {
            ChartUtils.writeChartAsPNG(it, chart, 1024, 768)
            InputFile(ByteArrayInputStream(it.toByteArray()), "stats.jpg")
        }
    }

    enum class StatsType(val type: String, val desc: String) {
        GIORNO("g", "oggi"),
        SETTIMANA("s", "questa settimana"),
        MESE("m", "questo mese"),
        ANNO("a", "tutto l'anno")
        ;

        companion object {
            private val byType = entries.associateBy { it.type }
            fun getByType(type: String): StatsType? {
                return byType[type]
            }
        }
    }

}
