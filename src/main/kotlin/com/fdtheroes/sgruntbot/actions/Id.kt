package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Id : Action {

    private val regex = Regex("^!id$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        if (regex.matches(message.text)) {
            BotUtils.instance.rispondi(message, "Il tuo id: ${message.from.id}")
        }
    }
}