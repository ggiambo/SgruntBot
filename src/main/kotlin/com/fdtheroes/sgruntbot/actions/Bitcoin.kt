package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot

abstract class Bitcoin(
    sgruntBot: SgruntBot,
    val botUtils: BotUtils,
    val mapper: ObjectMapper
) : HasHalp, Action(sgruntBot) {

    fun bitcoinvalue(currency: String): Double {
        val api = botUtils.textFromURL("https://blockchain.info/ticker")

        return mapper.readTree(api)
            .get(currency)
            .get("last").asDouble()
    }

}
