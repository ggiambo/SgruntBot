package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

//@Service
class Lamin(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex1 = Regex("(negr|negher)", RegexOption.IGNORE_CASE)
    private val regex2 = Regex("negrini", RegexOption.IGNORE_CASE)

    private val risposte = listOf(
        "Lamin mi manchi.",
        "RaSSista!",
        "Ordine Reich approves.",
    )

    override fun handle(message: Message) {
        if (regex1.containsMatchIn(message.text) && !regex2.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message(risposte.random()), message)
        }
    }
}
