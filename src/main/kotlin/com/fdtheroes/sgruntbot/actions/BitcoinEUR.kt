package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BitcoinEUR(
    botUtils: BotUtils,
    private val sgruntBot: SgruntBot
) : Bitcoin(botUtils), Action {

    private val regex = Regex("^!btce\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("EUR")
            val testo = "Il buttcoin vale $value EUR"
            sgruntBot.rispondi(message, testo)
        }
    }

    override fun halp() = "<b>!btce</b> per Bitcoin in EUR"
}
