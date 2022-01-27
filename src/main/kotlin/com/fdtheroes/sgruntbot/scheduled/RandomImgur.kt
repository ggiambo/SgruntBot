package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.google.gson.JsonParser
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RandomImgur(
    sendMessage: (SendMessage) -> Unit,
    private val imgurClientId: String,
) : RandomScheduledAction(sendMessage) {

    override fun getMessageText(): String {
        val viral = BotUtils.textFromURL(
            url = "https://api.imgur.com/3/gallery/hot/viral/0.json",
            properties = mapOf("Authorization" to "Client-ID $imgurClientId")
        )
        val randomEntry = JsonParser.parseString(viral).asJsonObject
            .get("data").asJsonArray
            .asSequence()
            .map { it.asJsonObject }
            .filter { !it.get("title")?.asString.isNullOrEmpty() }
            .filter { it.get("images")?.asJsonArray?.size() == 1 }
            .toList()
            .random()

        val title = randomEntry
            .get("title").asString
        val link = randomEntry
            .get("images").asJsonArray
            .first().asJsonObject
            .get("link").asString

        return "$title $link"
    }

}
