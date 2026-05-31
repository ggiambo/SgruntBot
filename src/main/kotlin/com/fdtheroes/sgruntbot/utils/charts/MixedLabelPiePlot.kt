package com.fdtheroes.sgruntbot.utils.charts

import org.jfree.chart.plot.PiePlot
import org.jfree.chart.plot.PiePlotState
import org.jfree.data.general.PieDataset
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

/**
 * Draw labels and simple labels mixed:
 * - Labels if value in percent is less than the supplied threshold
 * - Simple labels otherwise
 *
 * Warning: The dataset **must** be sorted by value
 */
class MixedLabelPiePlot<K : Comparable<K>>(
    dataset: PieDataset<K>,
    private val threshold: Double = 0.05,
) : PiePlot<K>(dataset) {

    override fun drawSimpleLabels(
        g2: Graphics2D,
        keys: List<K>,
        totalValue: Double,
        plotArea: Rectangle2D,
        pieArea: Rectangle2D,
        state: PiePlotState,
    ) = drawMixedLabels(g2, keys, totalValue, plotArea, pieArea, state)

    override fun drawLabels(
        g2: Graphics2D,
        keys: List<K>,
        totalValue: Double,
        plotArea: Rectangle2D,
        linkArea: Rectangle2D,
        state: PiePlotState,
    ) = drawMixedLabels(g2, keys, totalValue, plotArea, linkArea, state)

    private fun drawMixedLabels(
        g2: Graphics2D,
        keys: List<K>,
        totalValue: Double,
        plotArea: Rectangle2D,
        linkArea: Rectangle2D,
        state: PiePlotState,
    ) {
        val (underTreshold, overTreshold) = keys.partition {
            dataset.getValue(it).toDouble() / totalValue <= threshold
        }
        super.drawLabels(g2, underTreshold, totalValue, plotArea, linkArea, state)
        // rotate the labels by the angle of all "underTreshold" slices
        this.startAngle -= adjustStartAngle(underTreshold, totalValue)
        super.drawSimpleLabels(g2, overTreshold, totalValue, plotArea, linkArea, state)
    }

    private fun adjustStartAngle(small: List<K>, totalValue: Double) =
        365 * small.sumOf { dataset.getValue(it).toDouble() } / totalValue
}