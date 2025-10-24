package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.utils.BotUtils
import tools.jackson.databind.json.JsonMapper

abstract class Bitcoin(botUtils: BotUtils, botConfig: BotConfig, val jsonMapper: JsonMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    fun bitcoinvalue(currency: String): Double {
        val api = botUtils.textFromURL("https://blockchain.info/ticker")

        return jsonMapper.readTree(api)[currency]["last"].asDouble()
    }

}
