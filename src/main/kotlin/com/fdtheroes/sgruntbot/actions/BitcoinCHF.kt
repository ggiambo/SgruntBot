package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BitcoinCHF(botUtils: BotUtils, mapper: ObjectMapper) : Bitcoin(botUtils, mapper), Action {

    private val regex = Regex("^!btcc\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            val value = bitcoinvalue("CHF")
            val testo = "Il buttcoin vale $value denti d'oro"
            doNext(ActionResponse.message(testo))
        }
    }

    override fun halp() = "<b>!btcc</b> per Bitcoin in CHF"
}
