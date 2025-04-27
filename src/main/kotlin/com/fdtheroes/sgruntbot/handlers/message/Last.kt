package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Last(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig),
    HasHalp {

    private val regex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.matches(message.text) && botConfig.lastAuthor != null) {
            botUtils.rispondi(ActionResponse.message(botUtils.getUserLink(botConfig.lastAuthor)), message)
        }
    }

    override fun halp() = "<b>!last</b> uno slogan per l'ultimo autore"

}
