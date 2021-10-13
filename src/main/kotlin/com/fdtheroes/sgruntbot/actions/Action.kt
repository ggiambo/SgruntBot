package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

@FunctionalInterface
interface Action {
    fun doAction(message: Message, context: Context)
}