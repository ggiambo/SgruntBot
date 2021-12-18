package com.fdtheroes.sgruntbot.actions

import org.telegram.telegrambots.meta.api.objects.Message

@FunctionalInterface
interface Action {
    fun doAction(message: Message)
}