package com.fdtheroes.sgruntbot

import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime

class Context {

    var lastSuper: Message? = null
    var lastAuthor: String? = null
    var pignolo = false
    var pausedTime: LocalDateTime? = null

}