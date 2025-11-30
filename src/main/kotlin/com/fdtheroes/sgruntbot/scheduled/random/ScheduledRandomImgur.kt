package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.length
import com.fdtheroes.sgruntbot.utils.Disabled
import org.springframework.stereotype.Service
import tools.jackson.databind.json.JsonMapper

@Service
@Disabled
class ScheduledRandomImgur(
    botConfig: BotConfig,
    private val botUtils: BotUtils,
    private val jsonMapper: JsonMapper,
) : ScheduledRandom {

    // fake Client-ID, is enough
    private val headers = listOf(Pair("Authorization", "Client-ID ${botConfig.imgurClientId}"))

    override fun execute() {
        val viral = botUtils.textFromURL("https://api.imgur.com/3/gallery/hot/viral/0.json", headers = headers)
        val randomEntry = jsonMapper.readTree(viral)["data"].asSequence()
            .filter { !it["title"]?.asString().isNullOrEmpty() }
            .filter { it["images"]?.length() == 1L }
            .toList()
            .random()

        val title = randomEntry["title"].asString()
        val link = randomEntry["images"]
            .first()["link"].asString()

        val testo = "${title}\n${link}"

        botUtils.messaggio(ActionResponse.message(testo), false)
    }

}
