package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.telegram.telegrambots.meta.api.objects.Message

fun interface Action {
    fun doAction(message: Message, doNext: (ActionResponse) -> Unit)
}