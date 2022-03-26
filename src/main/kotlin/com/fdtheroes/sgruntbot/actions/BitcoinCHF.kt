package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BitcoinCHF(botUtils: BotUtils) : Bitcoin(botUtils), Action {

    private val regex = Regex("^!btcc\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("CHF")
            val testo = "Il buttcoin vale $value denti d'oro"
            sgruntBot.rispondi(message, testo)
        }
    }

    override fun halp() = "<b>!btcc</b> per Bitcoin in CHF"
}
