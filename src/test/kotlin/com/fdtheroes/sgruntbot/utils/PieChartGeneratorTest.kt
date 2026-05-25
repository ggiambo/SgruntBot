package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Stats
import com.fdtheroes.sgruntbot.utils.charts.PieChartGenerator
import org.assertj.core.api.Assertions.assertThat
import org.jfree.chart.LegendItem
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PieChartGeneratorTest : BaseTest() {
    val pieChartGenerator = PieChartGenerator(botUtils)

    @Test
    fun getChart() {
        val stats = (1..20).map {
            Stats(
                userId = it.toLong(),
                statDay = LocalDate.of(2026, 5, it),
                messages = it * 2,
                id = it.toLong() * 100,
            )
        }
        val pieChart = pieChartGenerator.getChart(stats, "Test con 20 stats")

        val iterator = pieChart.plot.legendItems.iterator()

        val labels = generateSequence { if (iterator.hasNext()) iterator.next() as LegendItem else null }
            .map { it.label }
            .toList()
        assertThat(labels)
            .allMatch { it.startsWith("Username_") }
    }

}