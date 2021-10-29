package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.json.JSONObject

abstract class Bitcoin {

    fun bitcoinvalue(currency: String): String {
        val api = BotUtils.textFromURL("https://blockchain.info/ticker")

        val value = JSONObject(api)
            .getJSONObject(currency)
            .getDouble("last")
        if (currency == "USD") {
            return "Il buttcoin vale $value dolla uno. Io faccio amole lungo lungo. Io tanta volia."
        }
        return "Il buttcoin vale $value $currency"
    }

}