package com.fdtheroes.sgruntbot.utils

import org.json.JSONObject
import java.net.URL

class MoneyUtils {

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

    fun bullshitInEuro(value: String?): Double {
        if (value == null) {
            return 0.0
        }
        val url = URL("https://free.currconv.com/api/v7/convert?q=BOB_EUR&compact=ultra&apiKey=60932c152410148d78dc")
            .openConnection()
            .getInputStream()
            .readAllBytes()
            .decodeToString()

        val res = JSONObject(url)
            .getDouble("BOB_EUR") * value.toDouble()
        return res
    }

}