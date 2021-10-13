package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

abstract class Action(val botUtils: BotUtils) {
    abstract fun doAction(message: Message, context: Context)
}