package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.io.ByteArrayInputStream

@Service
class Meteo(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!mete([oi])(.+)?", RegexOption.IGNORE_CASE)
    private val temperatureRegex = Regex("(\\d{1,2})°C")
    private val citta = listOf("Genova", "Tradate", "Guidonia", "Kollbrunn", "Legnano", "Pisa").sorted()

    override fun handle(message: Message) {
        val matchResult = regex.find(message.text)
        if (matchResult == null) {
            return
        }

        val listaCitta = getListaCitta(matchResult)
        if (listaCitta.isEmpty()) {
            return
        }

        val res = listaCitta
            .map { this.meteo(it) }
            .sortedByDescending { temperatureExtractor(it) }
            .joinToString(separator = "\n")

        botUtils.messaggio(ActionResponse.message(res))
    }

    private fun getListaCitta(matchResult: MatchResult): List<String> {
        val tipo = matchResult.groupValues[1]
        if (tipo == "i") {
            return citta
        }

        val citta = matchResult.groupValues[2].trim()
        if (citta.isEmpty()) {
            return emptyList()
        }
        return listOf(citta)
    }

    private fun meteo(citta: String): String {
        return botUtils.textFromURL(
            "https://wttr.in/$citta",
            listOf("format" to "%l: %c \uD83C\uDF21\uFE0F%t \uD83C\uDF2C\uFE0F%w \uD83D\uDCA6%h")
        ) {
            if (!it.isSuccessful) {
                ByteArrayInputStream("La città <a href=\"https://wttr.in/$citta\">$citta</a> non esiste. ".toByteArray())
            } else {
                it.body.byteStream()
            }
        }
    }

    private val temperatureExtractor = { meteoLine: String ->
        temperatureRegex.find(meteoLine)!!.groupValues[1].toInt()
    }

    override fun halp() = "<b>!meteo</b> <i>città</i> - Mostra le previsioni meteo per la città specificata."
}
