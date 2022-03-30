package com.fdtheroes.sgruntbot

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/rest/sgrunt/v1")
class SgruntController {

    @GetMapping("/lastAuthor")
    fun getLastAuthor(): User? {
        return Context.lastAuthor
    }

    @GetMapping("/lastSuper")
    fun getKastSuper(): User? {
        return Context.lastSuper
    }

    @GetMapping("/pignolo")
    fun isPignolo(): Boolean {
        return Context.pignolo
    }

    @GetMapping("/pausedTime")
    fun getPausedTime(): LocalDateTime? {
        return Context.pausedTime
    }

    @GetMapping("/randomScheduledActions")
    fun getRandomSchedules(): List<String> {
        return Context.randomScheduled.map {
            val randomScheduledAction = it.key
            val delay = it.value
            val duration =
                "${delay.toHours()} ore, ${delay.toMinutesPart()} minuti e ${delay.toSecondsPart()} secondi"
            val actionWhen =
                DateTimeFormatter.ofPattern("dd.MM.yyyy@HH:mm:ss").format(LocalDateTime.now().plus(delay))
            "Prossimo ${randomScheduledAction.simpleName} fra $duration, ovvero il $actionWhen"
        }
    }


}
