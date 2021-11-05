package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.Karma
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.util.*
import java.util.concurrent.TimeUnit


class ScheduledKarma {

    val mezzanotte = Calendar.getInstance().apply {
        this.set(Calendar.HOUR_OF_DAY, 0)
        this.set(Calendar.MINUTE, 0)
        this.set(Calendar.SECOND, 0)
    }.time

    fun start() {
        val oneDayInMilleseconds = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        Timer().schedule(PublishKarma(), mezzanotte, oneDayInMilleseconds)
    }

    inner class PublishKarma : TimerTask() {
        override fun run() {
            val testo = "*Karma Report*\n${Karma.testoKarma()}"
            val message = SendMessage().apply {
                this.text = testo
                this.chatId = BotUtils.chatId
                this.parseMode = ParseMode.MARKDOWN
            }

            BotUtils.rispondi(message)
        }
    }
}