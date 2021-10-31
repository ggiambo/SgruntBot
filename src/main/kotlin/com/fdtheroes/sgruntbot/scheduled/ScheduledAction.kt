package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random.Default.nextLong

abstract class ScheduledAction(
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
        val delay = nextLong(delayRangeInMillis.first, delayRangeInMillis.second)
        val delayHr = Duration.of(delay, ChronoUnit.MILLIS)
        log.info(getLogMessage(delayHr))
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

    private fun getLogMessage(delayHr: Duration): String {
        val duration = "${delayHr.toHours()} ore, ${delayHr.toMinutesPart()} minuti e ${delayHr.toSecondsPart()} secondi"
        val actionWhen = DateTimeFormatter.ofPattern("dd.MM.yyyy@HH:mm:ss").format(LocalDateTime.now().plus(delayHr))
        return "Prossimo $this.javaClass.simpleName fra $duration, ovvero il $actionWhen"
    }

    inner class SendAndReschedule : TimerTask() {
        override fun run() {
            buildAndSendMessage()
            scheduleNext()
        }
    }

}
