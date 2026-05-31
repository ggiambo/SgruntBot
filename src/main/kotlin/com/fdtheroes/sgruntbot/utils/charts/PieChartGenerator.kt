package com.fdtheroes.sgruntbot.utils.charts

import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.giambonini.MixedLabelPiePlot
import org.jfree.chart.JFreeChart
import org.jfree.chart.labels.StandardPieSectionLabelGenerator
import org.jfree.chart.ui.RectangleEdge
import org.jfree.chart.ui.RectangleInsets
import org.jfree.data.general.DefaultPieDataset
import org.springframework.stereotype.Service
import java.awt.Color

@Service
class PieChartGenerator(private val botUtils: BotUtils) {

    fun getChart(stats: List<Stats>, title: String): JFreeChart {
        val dataset = createDataset(stats.sortedBy { it.messages })
        val piePlot = MixedLabelPiePlot<String>(dataset, 0.05).apply {
            this.simpleLabels = true
            this.sectionOutlinesVisible = false
            this.isCircular = true
            dataset.keys.forEachIndexed { index, key ->
                this.setSectionPaint(key, seriesColors[index % seriesColors.size])
            }
            this.labelGenerator = StandardPieSectionLabelGenerator("{2} ({1})")
            this.legendLabelGenerator = StandardPieSectionLabelGenerator("{0}")
            this.labelFont = this.labelFont.deriveFont(20f)
        }

        val pieChart = JFreeChart(title, piePlot).apply {
            this.title.font = this.title.font.deriveFont(35f)
            this.legend.position = RectangleEdge.RIGHT
            this.legend.itemFont = this.legend.itemFont.deriveFont(20f)
            this.legend.padding = RectangleInsets(0.0,10.0,0.0,20.0)
            this.legend.itemLabelPadding = RectangleInsets(0.0,10.0,5.0,10.0)
        }

        return pieChart
    }

    private fun createDataset(stats: List<Stats>): DefaultPieDataset<String> {
        val getUserName = { stats: Stats -> botUtils.getUserName(botUtils.getChatMember(stats.userId)) }
        return DefaultPieDataset<String>().apply {
            stats.forEachIndexed { index, stat ->
                this.insertValue(index, getUserName(stat), stat.messages)
            }
        }
    }

    companion object {
        private fun colorFromString(color: String): Color {
            val colorAsInt = color
                .trim()
                .takeLast(6)
                .toInt(16)
            return Color(colorAsInt)
        }

        val seriesColors = arrayOf(
            colorFromString("#a6cee3"),
            colorFromString("#1f78b4"),
            colorFromString("#b2df8a"),
            colorFromString("#33a02c"),
            colorFromString("#fb9a99"),
            colorFromString("#e31a1c"),
            colorFromString("#fdbf6f"),
            colorFromString("#ff7f00"),
            colorFromString("#cab2d6"),
            colorFromString("#6a3d9a"),
        )

    }


}
