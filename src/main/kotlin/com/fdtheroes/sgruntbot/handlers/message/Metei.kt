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
    private val temperatureRegex = Regex("(\\d{1,2})°C")

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val res = citta
                .map { botUtils.textFromURL("https://wttr.in/$it", listOf("format" to "%l: %c \uD83C\uDF21\uFE0F%t \uD83C\uDF2C\uFE0F%w \uD83D\uDCA6%h")) }
                .sortedByDescending { temperatureExtractor(it) }
                .joinToString(separator = "\n")

            botUtils.messaggio(ActionResponse.message(res))
        }
    }

    private val temperatureExtractor = { meteoLine: String ->
        temperatureRegex.find(meteoLine)!!.groupValues[1].toInt()
    }

    override fun halp() = "<b>!metei</b> - Mostra le previsioni meteo per le nostre città più amate \uFE0F"
}
