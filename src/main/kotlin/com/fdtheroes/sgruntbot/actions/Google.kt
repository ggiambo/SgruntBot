package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Google : Action, HasHalp {

    private val regex = Regex("^!google (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            BotUtils.rispondi(
                message,
                """Cercatelo con <a href="https://www.google.com/search?q=$query">google</a> ritardato!™"""
            )

        }
    }

    override fun halp() = "<b>!google</b> <i>termine da cercare</i>"
}
