package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.telegram.telegrambots.meta.api.objects.Message

@FunctionalInterface
fun interface Action {
    fun doAction(message: Message, sgruntBot: SgruntBot)
}