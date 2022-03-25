package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BitcoinUSD(
    botUtils: BotUtils,
    private val sgruntBot: SgruntBot
) : Bitcoin(botUtils), Action {

    private val regex = Regex("(^!btc\$|quanto vale un bitcoin)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("USD")
            val testo = "Il buttcoin vale $value dolla uno. Io faccio amole lungo lungo. Io tanta volia."
            sgruntBot.rispondi(message, testo)
        }
    }

    override fun halp() = "<b>!btc</b> per Bitcoin in USD"
}
