package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import com.google.gson.JsonParser
import org.springframework.stereotype.Service

@Service
class RandomImgur(
    private val botUtils: BotUtils,
    sgruntBot: SgruntBot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    val imgurClientId = botConfig.imgurClientId

    override fun getMessageText(): String {
        val viral = botUtils.textFromURL(
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

        return "${title}\n${link}"
    }

}
