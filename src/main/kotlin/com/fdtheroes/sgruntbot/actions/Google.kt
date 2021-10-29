package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Google : Action {

    private val regex = Regex("^!google (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            BotUtils.rispondi(
                message,
                "Cercatelo con [google](https://www.google.com/search?q=$query) ritardato!™"
            )

        }
    }
}