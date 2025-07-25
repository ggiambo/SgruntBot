package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.message.Message

abstract class MessageHandler(protected val botUtils: BotUtils, protected val botConfig: BotConfig) : Handler {

    override suspend fun handle(update: Update) {
        handle(update.message)
    }

    abstract fun handle(message: Message)
}