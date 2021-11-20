package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.Karma
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*
import java.util.concurrent.TimeUnit

class ScheduledKarma : Timer() {

    val mezzanotte = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }.time

    fun start() {
        val oneDayInMilleseconds = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        Timer().schedule(PublishKarma(), mezzanotte, oneDayInMilleseconds)
    }

    inner class PublishKarma : TimerTask() {
        override fun run() {
            val message = SendMessage().apply {
                this.text = Karma.testoKarmaReport()
                this.chatId = BotUtils.chatId
                this.parseMode = ParseMode.HTML
            }

            BotUtils.rispondi(message)
        }
    }
}