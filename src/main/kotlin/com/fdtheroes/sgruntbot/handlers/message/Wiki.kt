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

    private val URL_titleAndURL = "https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=%s"
    private val URL_extract =
        "https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=%s"

    override fun handle(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            val titleAndURL = getTitleAndURL(query.urlEncode())
            val title = titleAndURL.first
            val url = titleAndURL.second
            if (title.isNullOrEmpty() || url.isNullOrEmpty()) {
                botUtils.rispondi(ActionResponse.message("Non c'è."), message)
                return
            }

            val testo = getExtract(title.urlEncode())
            if (testo.isNullOrEmpty()) {
                botUtils.rispondi(ActionResponse.message("Non c'è."), message)
                return
            }

            val risposta = "$testo\n$url"
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i>"

    private fun getTitleAndURL(query: String): Pair<String?, String?> {
        val testo =
            botUtils.textFromURL(URL_titleAndURL, listOf(query))
        val jsNode = mapper.readTree(testo)

        val titolo = jsNode[1][0]?.textValue()
        val url = jsNode[3][0]?.textValue()

        return Pair(titolo, url)
    }

    private fun getExtract(title: String): String? {
        val testo =
            botUtils.textFromURL(URL_extract, listOf(title))
        val jsNode = mapper.readTree(testo)["query"]["pages"]
            .elements()
            .next()["extract"]

        return jsNode?.textValue()
    }

}
