package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Metei(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!metei", RegexOption.IGNORE_CASE)

    private val citta = listOf("Genova", "Tradate", "Guidonia", "Kollbrunn", "Legnano", "Pisa").sorted()

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val res = citta.joinToString(separator = "") {
                botUtils.textFromURL("https://wttr.in/$it", listOf("format" to "4"))
            }
            botUtils.messaggio(ActionResponse.message(res))
        }
    }

    override fun halp() = "<b>!metei</b> - Mostra le previsioni meteo per le nostre città più amate \uFE0F"
}
