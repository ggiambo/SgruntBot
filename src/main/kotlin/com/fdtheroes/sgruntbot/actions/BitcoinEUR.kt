package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BitcoinEUR(botUtils: BotUtils, mapper: ObjectMapper) : Bitcoin(botUtils, mapper), Action {

    private val regex = Regex("^!btce\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("EUR")
            val testo = "Il buttcoin vale $value EUR"
            sgruntBot.rispondi(message, testo)
        }
    }

    override fun halp() = "<b>!btce</b> per Bitcoin in EUR"
}
