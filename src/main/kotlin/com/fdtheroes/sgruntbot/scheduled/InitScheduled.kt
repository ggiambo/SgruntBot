package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.toDate
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.reflect.KClass

@Service
class InitScheduled(private val scheduled: List<Scheduled>) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val nextScheduled = mutableMapOf<KClass<out Scheduled>, LocalDateTime>()

    @PostConstruct
    fun schedule() {
        scheduled.forEach {
            val firstRun = it.firstRun()
            log.info("Prima esecuzione di ${it.javaClass.simpleName} il $firstRun")
            nextScheduled.put(it::class, firstRun)
            Timer().schedule(timerTask { executeAndReschedule(it) }, firstRun.toDate())
        }
    }

    private fun executeAndReschedule(scheduled: Scheduled) {
        log.info("Eseguo ${scheduled.javaClass.simpleName}")
        scheduled.execute()
        val nextRun = scheduled.nextRun()
        log.info("Prossima esecuzione di ${scheduled.javaClass.simpleName} il $nextRun")
        nextScheduled.put(scheduled::class, nextRun)
        Timer().schedule(timerTask { executeAndReschedule(scheduled) }, nextRun.toDate())
    }

    fun getSchedulingInfo() : Map<KClass<out Scheduled>, LocalDateTime> = nextScheduled

}