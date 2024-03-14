package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.utils.BotUtils

abstract class Bitcoin(botUtils: BotUtils, botConfig: BotConfig, val mapper: ObjectMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    fun bitcoinvalue(currency: String): Double {
        val api = botUtils.textFromURL("https://blockchain.info/ticker")

        return mapper.readTree(api)[currency]["last"].asDouble()
    }

}
