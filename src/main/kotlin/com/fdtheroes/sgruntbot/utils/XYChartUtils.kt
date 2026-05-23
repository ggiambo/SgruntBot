package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.models.Stats
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.DateAxis
import org.jfree.chart.axis.DateTickUnit
import org.jfree.chart.axis.DateTickUnitType
import org.jfree.chart.plot.XYPlot
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.chart.ui.RectangleEdge
import org.jfree.data.time.Day
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection
import org.jfree.data.xy.XYDataset
import org.springframework.stereotype.Service
import java.awt.Color
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@Service
class XYChartUtils {

    fun xyChart(stats: List<Stats>): JFreeChart {
        val xyChart =
            ChartFactory.createTimeSeriesChart("Andamento settimanale", "Ultima settimana", "", createDataset(stats))
                .apply {
                    this.legend.position = RectangleEdge.RIGHT
                    this.backgroundPaint = Color.lightGray
                }

        (xyChart.plot as XYPlot).apply {
            (this.domainAxis as DateAxis).apply {
                this.dateFormatOverride = SimpleDateFormat("dd LLLL", Locale.ITALIAN)
                this.tickUnit = DateTickUnit(DateTickUnitType.DAY, 1)
            }
            (this.renderer as XYLineAndShapeRenderer).apply {
                this.defaultShapesVisible = true
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
                    this.add(getPeriod(stat.statDay!!), stat.messages)
                }
            })
        }
    }

}