package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.random.ScheduledCuloDiPapaSarru
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class CuloDiPapaSarru(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val scheduledCuloDiPapaSarru: ScheduledCuloDiPapaSarru,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!vangelo", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val results = scheduledCuloDiPapaSarru.getPrevious().joinToString(separator = "\n") {
                "- $it"
            }
            botUtils.rispondi(ActionResponse.message(results), message)
        }
    }

    override fun halp() =
        "<b>!vangelo</b> Il vangelo del CDP Sarru"
}