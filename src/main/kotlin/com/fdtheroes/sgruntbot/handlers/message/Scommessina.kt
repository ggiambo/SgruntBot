package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Scommessina(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex = Regex("^!scommess(in)?a$")

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botUtils.rispondi(
                ActionResponse.message("Lancio una moneta, se viene testa vinco io, se viene croce perdi tu."),
                message
            )
        }
    }
}
