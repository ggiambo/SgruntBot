package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.time.LocalDateTime

@Service
class Smetti(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex = Regex("^@?(sgrunty?|BlahBanf)(bot)? .*smetti.*", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botConfig.pausedTime = LocalDateTime.now().plusMinutes(5)
            botUtils.rispondi(ActionResponse.message("Ok, sto zitto 5 minuti. :("), message)
        }
    }
}
