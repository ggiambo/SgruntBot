package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Service
class ScheduledStats(
    private val botConfig: BotConfig,
    private val sgruntBot: SgruntBot,
    private val statsService: StatsService,
    private val statsAction: Stats,
) {

    val mezzanotte = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }.time

    @PostConstruct
    fun start() {
        val oneDayInMilleseconds = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        Timer().schedule(PublishStats(), mezzanotte, oneDayInMilleseconds)
    }

    inner class PublishStats : TimerTask() {
        override fun run() {
            val statsToday = statsService.getStatsThisMonth()
            val inputFile = statsAction.getStatsInputFile(statsToday, "Logorroici di questo mese", sgruntBot)


            val sendPhoto = SendPhoto()
            sendPhoto.chatId = botConfig.chatId
            sendPhoto.parseMode = ParseMode.HTML
            sendPhoto.photo = inputFile
            sgruntBot.rispondi(sendPhoto)
        }
    }

}