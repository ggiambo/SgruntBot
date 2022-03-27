package com.fdtheroes.sgruntbot.scheduled

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import com.fdtheroes.sgruntbot.BotUtils.Companion.length

@Service
class RandomImgur(
    private val botUtils: BotUtils,
    private val mapper: ObjectMapper,
    sgruntBot: SgruntBot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    val imgurClientId = botConfig.imgurClientId

    override fun getMessageText(): String {
        val viral = botUtils.textFromURL(
            url = "https://api.imgur.com/3/gallery/hot/viral/0.json",
            properties = mapOf("Authorization" to "Client-ID $imgurClientId")
        )
        val randomEntry = mapper.readTree(viral)
            .get("data").asSequence()
            .filter { !it.get("title")?.textValue().isNullOrEmpty() }
            .filter { it.get("images")?.length() == 1L }
            .toList()
            .random()

        val title = randomEntry
            .get("title").textValue()
        val link = randomEntry
            .get("images")
            .first()
            .get("link").textValue()

        return "${title}\n${link}"
    }

}
