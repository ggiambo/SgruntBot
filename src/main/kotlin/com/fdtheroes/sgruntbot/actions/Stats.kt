package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.persistence.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.knowm.xchart.BitmapEncoder
import org.knowm.xchart.PieChart
import org.knowm.xchart.style.PieStyler
import org.knowm.xchart.style.theme.GGPlot2Theme
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import java.awt.Color
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.annotation.PostConstruct
import javax.imageio.ImageIO

@Lazy
@Service
class Stats(
    private val statsService: StatsService,
    private val botUtils: BotUtils,
) : Action {

    private val regex = Regex("^!stats\$", RegexOption.IGNORE_CASE)
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

    private val pieChart = PieChart(1024, 768)

    @PostConstruct
    fun initChart() {
        pieChart.styler.theme = GGPlot2Theme()
        pieChart.styler.labelType = PieStyler.LabelType.Value
        pieChart.styler.setSliceBorderWidth(5.0)
        pieChart.styler.seriesColors = seriesColors
    }

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (!botUtils.isMessageInChat(message)) {
            return
        }

        if (message.from.isBot) {
            return
        }

        statsService.increaseStats(message.from.id)

        if (regex.matches(message.text)) {
            val statsToday = statsService.getStatsToday()
            val inputFile = getStatsInputFile(statsToday, "Logorroici di oggi", sgruntBot)

            val sendPhoto = SendPhoto()
            sendPhoto.chatId = message.chat.id.toString()
            sendPhoto.parseMode = ParseMode.HTML
            sendPhoto.photo = inputFile
            sgruntBot.rispondi(sendPhoto)
        }

    }

    fun getStatsInputFile(stats: List<Stats>, chartTitle: String, sgruntBot: SgruntBot): InputFile {
        val totalMessages = stats.sumOf { it.messages }
        pieChart.seriesMap.clear()
        stats.forEach { stat ->
            val userName = botUtils.getUserName(sgruntBot.getChatMember(stat.userId))
            val percentage = getPercentage(stat.messages, totalMessages)
            val name = "$userName $percentage%"
            pieChart.addSeries(name, stat.messages)
        }

        pieChart.title = chartTitle

        val image = BitmapEncoder.getBufferedImage(pieChart)
        val os = ByteArrayOutputStream()
        ImageIO.write(image, "png", os)
        return InputFile(ByteArrayInputStream(os.toByteArray()), "tappeto.jpg")
    }

    private fun colorFromString(color: String): Color {
        val colorAsInt = color
            .trim()
            .takeLast(6)
            .toInt(16)
        return Color(colorAsInt)
    }

    private fun getPercentage(messages: Int, total: Int): Int {
        return (messages * 100) / total
    }
}