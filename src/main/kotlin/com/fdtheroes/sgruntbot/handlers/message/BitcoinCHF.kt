package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BitcoinCHF(botUtils: BotUtils, botConfig: BotConfig, mapper: ObjectMapper) :
    Bitcoin(botUtils, botConfig, mapper) {

    private val regex = Regex("^!btcc\$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("CHF")
            val testo = "Il buttcoin vale $value denti d'oro"
            botUtils.rispondi(ActionResponse.message(testo), message)
        }
    }

    override fun halp() = "<b>!btcc</b> per Bitcoin in CHF"
}
