package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class BitcoinCHF(botUtils: BotUtils, mapper: ObjectMapper) : Bitcoin(botUtils, mapper), Action {

    private val regex = Regex("^!btcc\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text)) {
            val value = bitcoinvalue("CHF")
            val testo = "Il buttcoin vale $value denti d'oro"
            ctx.addResponse(ActionResponse.message(testo))
        }
    }

    override fun halp() = "<b>!btcc</b> per Bitcoin in CHF"
}
