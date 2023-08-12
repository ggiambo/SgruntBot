package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils.Companion.toDate
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.concurrent.timerTask

@Service
class InitScheduled(private val scheduled: List<Scheduled>) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    fun schedule() {
        scheduled.forEach {
            val firstRun = it.firstRun()
            log.info("Prima esecuzione di ${it.javaClass.simpleName} il $firstRun")
            Timer().schedule(timerTask { executeAndReschedule(it) }, firstRun.toDate())
        }
    }

    private fun executeAndReschedule(scheduled: Scheduled) {
        log.info("Eseguo ${scheduled.javaClass.simpleName}")
        scheduled.execute()
        val nextRun = scheduled.nextRun()
        log.info("Prossima esecuzione di ${scheduled.javaClass.simpleName} il $nextRun")
        Timer().schedule(timerTask { executeAndReschedule(scheduled) }, nextRun.toDate())
    }

}