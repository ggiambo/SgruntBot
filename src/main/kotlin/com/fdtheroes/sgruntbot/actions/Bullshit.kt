package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.json.JSONObject
import org.telegram.telegrambots.meta.api.objects.Message
import java.net.URL

class Bullshit : Action {

    private val regex = Regex("([0-9]+([,.][0-9]+)?)[ ]?(bs|bullshit)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        val value = regex.find(message.text)?.groupValues?.get(1)
        if (value != null) {
            val eur = bullshitInEuro(value)
            if (eur != 0.0) {
                BotUtils.instance.rispondi(message, "$value bullshit corrispondono a $eur pregiati euro.")
            } else {
                BotUtils.instance.rispondi(message, "Non ci riesco.")
            }
        }
    }

    private fun bullshitInEuro(value: String?): Double {
        if (value == null) {
            return 0.0
        }
        val url = URL("https://free.currconv.com/api/v7/convert?q=BOB_EUR&compact=ultra&apiKey=60932c152410148d78dc")
            .openConnection()
            .getInputStream()
            .readAllBytes()
            .decodeToString()

        return JSONObject(url)
            .getDouble("BOB_EUR") * value.toDouble()
    }
}