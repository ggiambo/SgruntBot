package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class BitcoinUSD(botUtils: BotUtils, botConfig: BotConfig, mapper: ObjectMapper) :
    Bitcoin(botUtils, botConfig, mapper) {

    private val regex = Regex("(^!btc\$|quanto vale un bitcoin)", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("USD")
            val testo = "Il buttcoin vale $value dolla uno. Io faccio amole lungo lungo. Io tanta volia."
            botUtils.rispondi(ActionResponse.message(testo), message)
        }
    }

    override fun halp() = "<b>!btc</b> per Bitcoin in USD"
}
