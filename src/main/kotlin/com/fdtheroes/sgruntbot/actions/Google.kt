package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Google(sgruntBot: SgruntBot) : Action(sgruntBot), HasHalp {

    private val regex = Regex("^!google (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            sgruntBot.rispondi(
                message,
                """Cercatelo con <a href="https://www.google.com/search?q=$query">google</a> ritardato!™"""
            )

        }
    }

    override fun halp() = "<b>!google</b> <i>termine da cercare</i>"
}
