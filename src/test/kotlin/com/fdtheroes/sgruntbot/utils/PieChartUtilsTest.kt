package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Stats
import org.assertj.core.api.Assertions.assertThat
import org.jfree.chart.LegendItem
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PieChartUtilsTest : BaseTest() {
    val pieChartUtils = PieChartUtils(botUtils)

    @Test
    fun pieChart() {
        val stats = (1..20).map {
            Stats(
                userId = it.toLong(),
                statDay = LocalDate.of(2026, 5, it),
                messages = it * 2,
                id = it.toLong() * 100,
            )
        }
        val pieChart = pieChartUtils.pieChart(stats, "Test con 20 stats")


        val iterator = pieChart.plot.legendItems.iterator() as Iterator<LegendItem>

        val labels = generateSequence { if (iterator.hasNext()) iterator.next() else null }
            .map { it.label }
            .toList()
        assertThat(labels)
            .allMatch { it.startsWith("Username_") }
            .allMatch { it.endsWith("%") }
    }

}