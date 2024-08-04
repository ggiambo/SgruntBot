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

    private val regex = Regex("^!(en)?wiki (.*)$", RegexOption.IGNORE_CASE)

    private val searchTitle = "https://%swikipedia.org/w/api.php?format=json&action=query&list=search&srsearch=%s"
    private val description =
        "https://%swikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=%s"
    private val urlByTitle = "https://%swikipedia.org/w/index.php?title=%s"

    override fun handle(message: Message) {
        val groups = regex.find(message.text)?.groupValues.orEmpty()
        if (groups.size == 3) {
            val lingua = if (groups[1].isNotEmpty()) "en." else "it."

            val query = groups[2]
            val title = getTitle(lingua, query.urlEncode())
            if (title.isNullOrEmpty()) {
                botUtils.rispondi(ActionResponse.message("Non c'è."), message)
                return
            }

            val description = getDescription(lingua, title)
            val url = String.format(urlByTitle, lingua, title)

            val risposta = "$description\n$url"
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i> (<b>!enwiki</b> per cercare in inglese)"

    private fun getTitle(lingua: String, query:String): String? {
        val searchTitleUrl = String.format(searchTitle, lingua, query)
        val testo = botUtils.textFromURL(searchTitleUrl)
        val jsNode = mapper.readTree(testo)

        val search = jsNode["query"]["search"].firstOrNull()
        if (search == null) {
            return null
        }

        return search["title"].textValue().urlEncode()
    }

    private fun getDescription(lingua: String, title: String): String {
        val descriptionUrl = String.format(description, lingua, title)
        val testo = botUtils.textFromURL(descriptionUrl)
        val jsNode = mapper.readTree(testo)
        return jsNode["query"]["pages"].first()["extract"].textValue()
    }

}
