package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.google.gson.JsonParser

abstract class Bitcoin: HasHalp {

    fun bitcoinvalue(currency: String): Double {
        val api = BotUtils.textFromURL("https://blockchain.info/ticker")

        return JsonParser.parseString(api).asJsonObject
            .get(currency).asJsonObject
            .get("last").asDouble
    }

}
