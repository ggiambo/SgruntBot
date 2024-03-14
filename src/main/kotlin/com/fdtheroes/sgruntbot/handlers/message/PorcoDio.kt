package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

//@Service
class PorcoDio(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex = Regex("\\bporco dio\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun handle(message: Message) {
        if (botConfig.pignolo && regex.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message("E la madooonna!"), message)
        }
    }

}
