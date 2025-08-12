package com.fdtheroes.sgruntbot.utils

import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.PieChart
import org.knowm.xchart.XYChart
import org.knowm.xchart.internal.chartpart.Chart
import org.knowm.xchart.style.PieStyler
import org.knowm.xchart.style.theme.GGPlot2Theme
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.awt.Color
import java.awt.Font
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object ChartUtils {

    fun pieChart(chartTitle: String): PieChart {
        return PieChart(1280, 1024).apply {
            this.title = chartTitle
            this.styler.theme = GGPlot2Theme()
            this.styler.seriesColors = seriesColors
            this.styler.labelType = PieStyler.LabelType.Value
            this.styler.setSliceBorderWidth(5.0)
            this.styler.chartTitleFont = Font("Ubuntu Regular", Font.PLAIN, 14)
            this.styler.legendFont = Font("Ubuntu Regular", Font.PLAIN, 14)
        }
    }


    private val seriesColors = arrayOf(
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

    fun getAsInputFile(chart: Chart<*, *>): InputFile {
        val image = BitmapEncoder.getBufferedImage(chart)
        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        return InputFile(ByteArrayInputStream(os.toByteArray()), "stats.jpg")
    }

    private fun colorFromString(color: String): Color {
        val colorAsInt = color
            .trim()
            .takeLast(6)
            .toInt(16)
        return Color(colorAsInt)
    }
}