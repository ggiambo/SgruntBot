package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Halp(botUtils: BotUtils, botConfig: BotConfig, private val halp: List<HasHalp>) :
    MessageHandler(botUtils, botConfig) {

    private val regex = Regex("^!help", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val risposta = halp
                .sortedBy { it.javaClass.simpleName }
                .joinToString("\n") { it.halp() }
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }
}
