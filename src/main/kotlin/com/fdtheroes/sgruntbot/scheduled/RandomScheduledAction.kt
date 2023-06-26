package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random.Default.nextLong

abstract class RandomScheduledAction(val sgruntBot: Bot, val botConfig: BotConfig) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val timer = Timer()
    private val delayRangeInMillis = Pair(
        Duration.of(16, ChronoUnit.HOURS).toMillis(),
        Duration.of(36, ChronoUnit.HOURS).toMillis()
    )

    var nextScheduled: LocalDateTime? = null
    abstract fun getMessageText(): String

    @PostConstruct
    fun start() {
        scheduleNext()
    }

    private fun scheduleNext() {
        val delay = nextLong(delayRangeInMillis.first, delayRangeInMillis.second)
        val delayHr = Duration.of(delay, ChronoUnit.MILLIS)
        this.nextScheduled = LocalDateTime.now().plus(delayHr)
        log.info(getLogMessage(delayHr))
        timer.schedule(SendAndReschedule(), delay)
    }

    private fun buildAndSendMessage() {
        val text = getMessageText()
        if (text.isEmpty()) {
            return
        }

        sgruntBot.messaggio(ActionResponse.message(text))
    }

    private fun getLogMessage(delayHr: Duration): String {
        val duration =
            "${delayHr.toHours()} ore, ${delayHr.toMinutesPart()} minuti e ${delayHr.toSecondsPart()} secondi"
        val actionWhen = DateTimeFormatter.ofPattern("dd.MM.yyyy@HH:mm:ss").format(nextScheduled)
        return "Prossimo ${this.javaClass.simpleName} fra $duration, ovvero il $actionWhen"
    }

    inner class SendAndReschedule : TimerTask() {
        override fun run() {
            buildAndSendMessage()
            scheduleNext()
        }
    }

}
