package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.json.JSONObject

abstract class Bitcoin {

    fun bitcoinvalue(currency: String): Double {
        val api = BotUtils.textFromURL("https://blockchain.info/ticker")

        return JSONObject(api)
            .getJSONObject(currency)
            .getDouble("last")
    }

}