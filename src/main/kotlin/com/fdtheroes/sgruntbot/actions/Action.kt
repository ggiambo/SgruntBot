package com.fdtheroes.sgruntbot.actions

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.telegram.telegrambots.meta.api.objects.Message

@FunctionalInterface
interface Action {
    fun doAction(message: Message)
    fun doActionAsync(message: Message) = GlobalScope.launch {
        doAction(message)
    }
}