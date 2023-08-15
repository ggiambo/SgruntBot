package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.toDate
import com.fdtheroes.sgruntbot.ChartUtils
import com.fdtheroes.sgruntbot.ChartUtils.getAsInputFile
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.theme.GGPlot2Theme
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.awt.BasicStroke

@Service
class ScheduledStats(
    private val sgruntBot: Bot,
    private val botUtils: BotUtils,
    private val statsService: StatsService,
) : ScheduledAMezzanotte {

    private val chart = XYChart(1280, 1024).apply {
        this.styler.theme = GGPlot2Theme()
        this.styler.seriesColors = ChartUtils.seriesColors
        this.title = "Logorroici degli ultimi 15 giorni"
        this.styler.theme = GGPlot2Theme()
        this.styler.isToolTipsEnabled = false
        this.styler.seriesColors = ChartUtils.seriesColors
        this.styler.datePattern = "d"
    }
    private val seriesStroke = BasicStroke(6F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)

    override fun execute() {
        val statsLast15Days = statsService.getStatsLastDays(15).groupBy { it.userId }

        val inputFile = getStatsInputFile(statsLast15Days)
        val actionResponse = ActionResponse.photo("", inputFile, false)

        sgruntBot.messaggio(actionResponse)
    }

    private fun getStatsInputFile(statsThisMonthByUserId: Map<Long, List<Stats>>): InputFile {
        chart.seriesMap.clear()
        statsThisMonthByUserId.forEach {
            val series = chart.addSeries(
                getSerieName(it.key),
                it.value.map { stats -> stats.statDay.toDate() },
                it.value.map { stats -> stats.messages },
            )
            series.lineStyle = seriesStroke
            series.isSmooth = true
        }

        return getAsInputFile(chart)
    }

    private fun getSerieName(userId: Long): String {
        val serieName = botUtils.getUserName(sgruntBot.getChatMember(userId))
        if (serieName.isNotEmpty()) {
            return serieName
        }
        return userId.toString()
    }

}
