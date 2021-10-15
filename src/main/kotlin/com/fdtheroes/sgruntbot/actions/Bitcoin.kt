package com.fdtheroes.sgruntbot.actions

import org.json.JSONObject
import java.net.URL

abstract class Bitcoin {

    fun bitcoinvalue(currency: String): String {
        val api = URL("https://blockchain.info/ticker")
            .openConnection()
            .getInputStream()
            .readAllBytes()
            .decodeToString()

        val value = JSONObject(api)
            .getJSONObject(currency)
            .getDouble("last")
        if (currency == "USD") {
            return "Il buttcoin vale $value dolla uno. Io faccio amole lungo lungo. Io tanta volia."
        }
        return "Il buttcoin vale $value $currency"
    }

}