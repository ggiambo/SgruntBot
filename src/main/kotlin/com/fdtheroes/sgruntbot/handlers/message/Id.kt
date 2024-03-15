package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Id(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!id$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.matches(message.text)) {
            botUtils.rispondi(ActionResponse.message("Il tuo id: ${message.from.id}"), message)
        }
    }

    override fun halp() = "<b>!id</b> mostra il tuo id di Telegram"
}
