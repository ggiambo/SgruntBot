package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.toDate
import com.fdtheroes.sgruntbot.ChartUtils
import com.fdtheroes.sgruntbot.ChartUtils.getAsInputFile
import com.fdtheroes.sgruntbot.actions.models.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import jakarta.annotation.PostConstruct
import org.knowm.xchart.XYChart
import org.knowm.xchart.style.theme.GGPlot2Theme
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.awt.BasicStroke
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class ScheduledStats(
    private val botConfig: BotConfig,
    private val sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
    private val statsService: StatsService,
) {

    private val mezzanotte = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }.time

    private val chart = XYChart(1280, 1024)
    private val seriesStroke = BasicStroke(6F, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER)


    @PostConstruct
    fun start() {
        chart.styler.theme = GGPlot2Theme()
        chart.styler.seriesColors = ChartUtils.seriesColors
        chart.title = "Logorroici degli ultimi 15 giorni"
        chart.styler.theme = GGPlot2Theme()
        chart.styler.isToolTipsEnabled = false
        chart.styler.seriesColors = ChartUtils.seriesColors
        chart.styler.datePattern = "d"

        val oneDayInMilleseconds = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        Timer().schedule(PublishStats(), mezzanotte, oneDayInMilleseconds)
    }

    inner class PublishStats : TimerTask() {
        override fun run() {
            val statsLast15Days = statsService.getStatsLastDays(15)
                .groupBy { it.userId }
            val inputFile = getStatsInputFile(statsLast15Days)

            val sendPhoto = SendPhoto()
            sendPhoto.chatId = botConfig.chatId
            sendPhoto.parseMode = ParseMode.HTML
            sendPhoto.photo = inputFile
            sgruntBot.rispondi(sendPhoto)
        }
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

    fun getSerieName(userId: Long): String {
        val serieName = botUtils.getUserName(sgruntBot.getChatMember(userId))
        if (serieName.isNotEmpty()) {
            return serieName
        }
        return userId.toString()
    }

}