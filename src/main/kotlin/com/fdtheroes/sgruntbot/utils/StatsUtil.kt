package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.persistence.StatsService
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.toDate
import org.jfree.chart.ChartUtils
import org.jfree.chart.JFreeChart
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.XYChart
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.util.*
import javax.imageio.ImageIO

@Service
class StatsUtil(
    private val statsService: StatsService,
    private val pieChartUtils: PieChartUtils,
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
        val statsByDay = statsService.getStatsFromDate(sevenDaysAgo).groupBy { it.statDay }
        val stats = statsByDay.map { (day, statsOfDay) ->
            statsOfDay.fold(Pair(day, 0)) { acc, next -> Pair(day, acc.second + next.messages) }
        }

        val chart = XYChart(1280, 1024).apply {
            this.title = "Andamento settimanale"
            this.xAxisTitle = "Ultima settimana"
            this.addSeries("Messaggi totali", stats.map { it.first.toDate() }, stats.map { it.second })
            this.styler.locale = Locale.ITALIAN
            this.styler.datePattern = "dd MMMM"
        }

        val image = BitmapEncoder.getBufferedImage(chart)
        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        return InputFile(ByteArrayInputStream(os.toByteArray()), "stats.jpg")
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
