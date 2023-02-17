package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.telegram.telegrambots.meta.api.objects.Message

abstract class Action(val sgruntBot: SgruntBot) {
    abstract fun doAction(message: Message)
}