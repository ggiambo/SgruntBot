package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Rogan : Action {

    private val regex = Regex("rogan", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            BotUtils.rispondi(message, "Cheppalle! Yawn!")
        }
    }
}