package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.urlEncode
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Wiki(botUtils: BotUtils, botConfig: BotConfig, val mapper: ObjectMapper) : MessageHandler(botUtils, botConfig),
    HasHalp {

    private val regex = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)

    private val searchTitle = "https://it.wikipedia.org/w/api.php?format=json&action=query&list=search&srsearch=%s"
    private val description =
        "https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=%s"
    private val urlByTitle = "https://it.wikipedia.org/w/index.php?title=%s"

    override fun handle(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            val title = getTitle(query.urlEncode())
            if (title.isNullOrEmpty()) {
                botUtils.rispondi(ActionResponse.message("Non c'è."), message)
                return
            }

            val description = getDescription(title)
            val url = String.format(urlByTitle, title)

            val risposta = "$description\n$url"
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i>"

    private fun getTitle(query: String): String? {
        val testo = botUtils.textFromURL(searchTitle, listOf(query))
        val jsNode = mapper.readTree(testo)

        val search = jsNode["query"]["search"].firstOrNull()
        if (search == null) {
            return null
        }

        return search["title"].textValue().urlEncode()
    }

    private fun getDescription(title: String): String {
        val testo = botUtils.textFromURL(description, listOf(title))
        val jsNode = mapper.readTree(testo)
        return jsNode["query"]["pages"].first()["extract"].textValue()
    }

}
