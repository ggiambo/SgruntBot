package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.utils.BotUtils

abstract class Bitcoin(val botUtils: BotUtils, val mapper: ObjectMapper) : HasHalp {

    fun bitcoinvalue(currency: String): Double {
        val api = botUtils.textFromURL("https://blockchain.info/ticker")

        return mapper.readTree(api)[currency]["last"].asDouble()
    }

}
