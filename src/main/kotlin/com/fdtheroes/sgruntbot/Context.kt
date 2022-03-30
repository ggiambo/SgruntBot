package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.scheduled.RandomScheduledAction
import org.telegram.telegrambots.meta.api.objects.User
import java.time.Duration
import java.time.LocalDateTime
import kotlin.reflect.KClass

object Context {
    var lastSuper: User? = null
    var lastAuthor: User? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null
    var randomScheduled = mutableMapOf<KClass<out RandomScheduledAction>, Duration>()

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
        randomScheduled = mutableMapOf()
    }

    fun nextScheduled(randomScheduledAction: KClass<out RandomScheduledAction>, delayFromNow: Duration) {
        randomScheduled[randomScheduledAction] = delayFromNow
    }
}
