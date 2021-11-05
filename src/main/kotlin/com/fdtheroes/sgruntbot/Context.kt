package com.fdtheroes.sgruntbot

import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDateTime

object Context {
    var lastSuper: User? = null
    var lastAuthor: String? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
    }
}