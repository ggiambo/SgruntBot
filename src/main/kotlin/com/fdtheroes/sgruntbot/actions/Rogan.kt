package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Rogan : Action {

    private val regex = Regex("rogan", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        if (regex.containsMatchIn(message.text)) {
            BotUtils.instance.rispondi(message, "Cheppalle! Yawn!")
        }
    }
}