package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random

@Service
class Ironic(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    override fun handle(message: Message) {
        if (message.text.length <= 50 && Random.nextInt(200) == 0) {
            botUtils.rispondi(ActionResponse.message(ironic(message.text)), message)
        }
    }

    fun ironic(text: String): String {
        return text.uppercase().mapIndexed { index, c ->
            if (index % 2 == 0) c.lowercaseChar() else c
        }.joinToString(
            separator = "",
            prefix = "\"",
            postfix = "\"",
        )
    }

}