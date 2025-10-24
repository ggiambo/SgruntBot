package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import tools.jackson.databind.json.JsonMapper
import java.net.URLEncoder

@Service
class Wiki(botUtils: BotUtils, botConfig: BotConfig, val jsonMapper: JsonMapper) : MessageHandler(botUtils, botConfig),
    HasHalp {

    private val regex = Regex("^!(en)?wiki (.*)$", RegexOption.IGNORE_CASE)

    private val wikipediaApi = "https://%swikipedia.org/w/api.php"
    private val urlByTitle = "https://%swikipedia.org/w/index.php?title=%s"
    private val wikipediaHeaders = listOf("user-agent" to "SgruntBot/1.0")

    override fun handle(message: Message) {
        val groups = regex.find(message.text)?.groupValues.orEmpty()
        if (groups.size == 3) {
            val lingua = if (groups[1].isNotEmpty()) "en." else "it."

            val query = groups[2]
            val title = getTitle(lingua, query)
            if (title.isNullOrEmpty()) {
                botUtils.rispondi(ActionResponse.message("Non c'è."), message)
                return
            }

            val description = getDescription(lingua, title)
            val url = String.format(urlByTitle, lingua, URLEncoder.encode(title, Charsets.UTF_8))

            val risposta = "$description\n$url"
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i> (<b>!enwiki</b> per cercare in inglese)"

    private fun getTitle(lingua: String, query: String): String? {
        val testo = botUtils.textFromURL(
            url = String.format(wikipediaApi, lingua),
            params = listOf(
                "format" to "json",
                "action" to "query",
                "exintro" to "",
                "explaintext" to "",
                "list" to "search",
                "srsearch" to query
            ),
            headers = wikipediaHeaders
        )
        val jsNode = jsonMapper.readTree(testo)

        val search = jsNode["query"]["search"].firstOrNull()
        if (search == null) {
            return null
        }

        return search["title"].asString()
    }

    private fun getDescription(lingua: String, title: String): String {
        val testo = botUtils.textFromURL(
            url = String.format(wikipediaApi, lingua),
            params = listOf(
                "format" to "json",
                "action" to "query",
                "prop" to "extracts",
                "exintro" to "",
                "explaintext" to "",
                "redirects" to "1",
                "titles" to title
            ),
            headers = wikipediaHeaders
        )
        val jsNode = jsonMapper.readTree(testo)
        return jsNode["query"]["pages"].first()["extract"].asString()
    }

}
