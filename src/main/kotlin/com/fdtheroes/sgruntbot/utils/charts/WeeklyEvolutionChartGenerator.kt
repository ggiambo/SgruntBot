package com.fdtheroes.sgruntbot.utils.charts

import com.fdtheroes.sgruntbot.models.Stats
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.axis.DateTickMarkPosition
import org.jfree.chart.axis.DateTickUnit
import org.jfree.chart.axis.DateTickUnitType
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.ui.RectangleEdge
import org.jfree.data.time.Day
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.xy.XYDataset
import org.springframework.stereotype.Service
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Service
class WeeklyEvolutionChartGenerator {

    fun getChart(stats: List<Stats>): JFreeChart {
        val xyChart =
            ChartFactory.createTimeSeriesChart("Andamento settimanale", "", "Messaggi", createDataset(stats))
                .apply {
                    this.legend.visible = false
                    this.backgroundPaint = Color.lightGray
                    this.title.font = this.title.font.deriveFont(35f)
                }

        (xyChart.plot as XYPlot).apply {
            (this.domainAxis as DateAxis).apply {
                this.dateFormatOverride = SimpleDateFormat("EEEE", Locale.ITALIAN)
                this.tickUnit = DateTickUnit(DateTickUnitType.DAY, 1)
                this.tickLabelFont = this.tickLabelFont.deriveFont(20f)
                this.isVerticalTickLabels = true
            }
            (this.rangeAxis as NumberAxis).apply {
                this.tickLabelFont = this.tickLabelFont.deriveFont(20f)
                this.labelFont = this.tickLabelFont.deriveFont(25f).deriveFont(Font.BOLD)
            }
            (this.renderer as XYLineAndShapeRenderer).apply {
                this.setSeriesStroke(0, BasicStroke(15f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
                this.drawSeriesLineAsPath = true
            }
            this.backgroundPaint = Color.white
            this.rangeGridlinePaint = Color.lightGray
            this.domainGridlinePaint = Color.lightGray
        }

        return xyChart
    }

    private fun createDataset(stats: List<Stats>): XYDataset {
        val getPeriod = { statsDay: LocalDate -> Day(statsDay.dayOfMonth, statsDay.monthValue, statsDay.year) }
        return TimeSeriesCollection().apply {
            this.addSeries(TimeSeries("Messaggi totali").apply {
                stats.forEach { stat ->
                    this.add(getPeriod(stat.statDay), stat.messages)
                }
            })
        }
    }

}