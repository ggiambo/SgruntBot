package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Google(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!google (.*)$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            botUtils.rispondi(
                ActionResponse.message("""Cercatelo con <a href="https://www.google.com/search?q=$query">google</a> ritardato!™"""),
                message
            )
        }
    }

    override fun halp() = "<b>!google</b> <i>termine da cercare</i>"
}
