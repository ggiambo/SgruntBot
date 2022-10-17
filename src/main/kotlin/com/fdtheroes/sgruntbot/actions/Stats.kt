package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.ChartUtils
import com.fdtheroes.sgruntbot.ChartUtils.getAsInputFile
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.persistence.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import jakarta.annotation.PostConstruct
import org.knowm.xchart.PieChart
import org.knowm.xchart.style.PieStyler
import org.knowm.xchart.style.theme.GGPlot2Theme
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message

@Lazy
@Service
class Stats(
    private val statsService: StatsService,
    private val botUtils: BotUtils,
) : Action, HasHalp {

    private val regex = Regex("^!stats(.*)\$", RegexOption.IGNORE_CASE)

    private val pieChart = PieChart(1280, 1024)

    @PostConstruct
    fun initChart() {
        pieChart.styler.theme = GGPlot2Theme()
        pieChart.styler.labelType = PieStyler.LabelType.Value
        pieChart.styler.setSliceBorderWidth(5.0)
        pieChart.styler.seriesColors = ChartUtils.seriesColors
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
            val tipo = StatsType.getByType(regex.find(message.text)?.groupValues?.get(1))
            val stats = when (tipo) {
                StatsType.GIORNO -> statsService.getStatsToday()
                StatsType.SETTIMANA -> statsService.getStatsThisWeek()
                StatsType.MESE -> statsService.getStatsThisMonth()
                StatsType.ANNO -> statsService.getStatsThisYear()
            }
            val inputFile = getStatsInputFile(stats, "Logorroici di ${tipo.desc}", sgruntBot)

            val sendPhoto = SendPhoto()
            sendPhoto.chatId = message.chat.id.toString()
            sendPhoto.parseMode = ParseMode.HTML
            sendPhoto.photo = inputFile
            sgruntBot.rispondi(sendPhoto)
        }
    }

    private fun getStatsInputFile(stats: List<Stats>, chartTitle: String, sgruntBot: SgruntBot): InputFile {
        val totalMessages = stats.sumOf { it.messages }
        pieChart.title = chartTitle
        pieChart.seriesMap.clear()
        stats
            .sortedBy { it.messages }
            .asReversed()
            .forEach { stat ->
                val userName = botUtils.getUserName(sgruntBot.getChatMember(stat.userId))
                val percentage = getPercentage(stat.messages, totalMessages)
                val name = "$userName $percentage%"
                pieChart.addSeries(name, stat.messages)
            }


        return getAsInputFile(pieChart)
    }

    private fun getPercentage(messages: Int, total: Int): Int {
        return (messages * 100) / total
    }

    enum class StatsType(val type: String, val desc: String) {
        GIORNO("g", "oggi"),
        SETTIMANA("s", "questa settimana"),
        MESE("m", "questo mese"),
        ANNO("a", "tutto l'anno")
        ;

        companion object {
            private val byType = StatsType.values().associateBy { it.type }
            fun getByType(type: String?): StatsType {
                val tipo = type.orEmpty().trim().lowercase()
                if (tipo.isEmpty()) {
                    return GIORNO
                }
                return byType.getValue(tipo)
            }
        }
    }

    override fun halp() = """
        <b>!stats <i>tipo</i></b> le stats dei logorroici, dove <i>tipo</i>
        <b>g</b> stats di questa giornata
        <b>s</b> stats di questa settimana
        <b>m</b> stats di questo mese
        <b>a</b> stats di quest'anno
        """.trimIndent()

}