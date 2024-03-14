package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class DioPorco(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex = Regex("\\bdio (porco|cane)\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun handle(message: Message) {
        if (botConfig.pignolo && regex.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message("Che mi tocca sentire!"), message)
        }
    }

}
