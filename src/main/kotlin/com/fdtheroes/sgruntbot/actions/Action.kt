package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.telegram.telegrambots.meta.api.objects.Message

@FunctionalInterface
interface Action {
    fun doAction(message: Message, context: Context)
    fun doActionAsync(message: Message, context: Context) = GlobalScope.launch {
        doAction(message, context)
    }
}