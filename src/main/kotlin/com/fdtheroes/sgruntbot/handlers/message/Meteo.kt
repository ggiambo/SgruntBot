package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Meteo(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!meteo(.*)", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val citta = regex.find(message.text)?.groupValues?.get(1).orEmpty().trim()
        if (citta.isEmpty()) {
            return
        }
        val meteo = botUtils.textFromURL("https://wttr.in/$citta", listOf("format" to "4"))
        botUtils.rispondi(ActionResponse.message(meteo), message)
    }

    override fun halp() = "<b>!meteo</b> <i>città</i> - Mostra le previsioni meteo per la città specificata."
}
