package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import tools.jackson.databind.json.JsonMapper

@Service
class BitcoinEUR(botUtils: BotUtils, botConfig: BotConfig, jsonMapper: JsonMapper) :
    Bitcoin(botUtils, botConfig, jsonMapper) {

    private val regex = Regex("^!btce\$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("EUR")
            val testo = "Il buttcoin vale $value EUR"
            botUtils.rispondi(ActionResponse.message(testo), message)
        }
    }

    override fun halp() = "<b>!btce</b> per Bitcoin in EUR"
}
