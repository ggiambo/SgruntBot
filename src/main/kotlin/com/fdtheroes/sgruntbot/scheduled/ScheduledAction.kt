package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random

abstract class ScheduledAction(
    val context: Context,
    private val sendMessage: (SendMessage) -> Unit
) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val timer = Timer()
    private val delayRangeInMillis = Pair(
        Duration.of(16, ChronoUnit.HOURS).toMillis(),
        Duration.of(36, ChronoUnit.HOURS).toMillis()
    )

    abstract fun getMessageText(): String

    fun start() {
        scheduleNext()
    }

    private fun scheduleNext() {
        val delay = Random.nextLong(delayRangeInMillis.first, delayRangeInMillis.second)
        val delayHr = Duration.of(delay, ChronoUnit.MILLIS)
        log.info("Prossimo ${this.javaClass.simpleName} fra ${delayHr.toHours()} ore, ${delayHr.toMinutesPart()} minuti e ${delayHr.toSecondsPart()} secondi")
        timer.schedule(SendAndReschedule(), delay)
    }

    private fun buildAndSendMessage() {
        val text = getMessageText()
        if (text.isEmpty()) {
            return
        }

        val message = SendMessage().apply {
            this.text = text
            this.chatId = BotUtils.chatId
        }

        sendMessage(message)
    }

    inner class SendAndReschedule : TimerTask() {
        override fun run() {
            buildAndSendMessage()
            scheduleNext()
        }
    }

}
