package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class BitcoinEUR : Bitcoin(), Action {

    private val regex = Regex("^!btce\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("EUR")
            val testo = "Il buttcoin vale $value EUR"
            BotUtils.rispondi(message, testo)
        }
    }

    override fun halp() = "<b>!btce</b> per Bitcoin in EUR"
}