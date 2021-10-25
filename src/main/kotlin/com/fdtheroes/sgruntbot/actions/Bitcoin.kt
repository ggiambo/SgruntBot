package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.json.JSONObject
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL


abstract class Bitcoin {

    fun bitcoinvalue(currency: String): String {
        val api = BotUtils.instance.textFromURL("https://blockchain.info/ticker")

        val value = JSONObject(api)
            .getJSONObject(currency)
            .getDouble("last")
        if (currency == "USD") {
            return "Il buttcoin vale $value dolla uno. Io faccio amole lungo lungo. Io tanta volia."
        }
        return "Il buttcoin vale $value $currency"
    }

}