package com.fdtheroes.sgruntbot.scheduled.random

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.length
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class ScheduledRandomImgur(
    private val botUtils: BotUtils,
    private val mapper: ObjectMapper,
    private val sgruntBot: Bot,
    botConfig: BotConfig,
) : ScheduledRandom {

    // fake Client-ID, is enough
    private val headers = listOf(Pair("Authorization", "Client-ID ${botConfig.clientId}"))

    override fun execute() {
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

        val testo = "${title}\n${link}"

        sgruntBot.messaggio(ActionResponse.message(testo, false))
    }

}
