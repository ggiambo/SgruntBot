package com.fdtheroes.sgruntbot.scheduled.random

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.length
import com.fdtheroes.sgruntbot.actions.models.ActionResponse

//@Service
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
        val randomEntry = mapper.readTree(viral)["data"].asSequence()
            .filter { !it["title"]?.textValue().isNullOrEmpty() }
            .filter { it["images"]?.length() == 1L }
            .toList()
            .random()

        val title = randomEntry["title"].textValue()
        val link = randomEntry["images"]
            .first()["link"].textValue()

        val testo = "${title}\n${link}"

        sgruntBot.messaggio(ActionResponse.message(testo, false))
    }

}
