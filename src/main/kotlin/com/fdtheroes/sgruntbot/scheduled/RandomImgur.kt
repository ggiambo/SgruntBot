package com.fdtheroes.sgruntbot.scheduled

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import com.fdtheroes.sgruntbot.BotUtils.Companion.length
import com.fdtheroes.sgruntbot.Bot

@Service
class RandomImgur(
    private val botUtils: BotUtils,
    private val mapper: ObjectMapper,
    sgruntBot: Bot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    // fake Client-ID, is enough
    private val headers = listOf(Pair("Authorization", "Client-ID ${botConfig.clientId}"))

    override fun getMessageText(): String{
        val viral = botUtils.textFromURL("https://api.imgur.com/3/gallery/hot/viral/0.json", headers = headers)
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
