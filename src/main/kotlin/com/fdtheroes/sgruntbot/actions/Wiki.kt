package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.urlEncode
import com.fdtheroes.sgruntbot.SgruntBot
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Wiki(private val botUtils: BotUtils) : Action, HasHalp {

    private val regex = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            val first = getSearchResponse(query.urlEncode())
            if (first.isEmpty()) {
                return sgruntBot.rispondi(message, "Non c'è.")
            }

            getResponsePages(first.urlEncode()).forEach {
                val testo = it.get("extract").asString
                val title = it.get("title").asString
                val risposta = "$testo\nhttps://it.wikipedia.org/wiki/${title.urlEncode()}"
                sgruntBot.rispondi(message, risposta)
            }
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i>"

    private fun getSearchResponse(query: String): String {
        val url =
            botUtils.textFromURL("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=$query")
        return JsonParser.parseString(url).asJsonArray
            .get(1).asJsonArray
            .get(0).asString
    }

    private fun getResponsePages(titles: String): Sequence<JsonObject> {
        val url =
            botUtils.textFromURL("https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=$titles")

        val pages = JsonParser.parseString(url).asJsonObject
            .get("query").asJsonObject
            .get("pages").asJsonObject

        return pages.keySet().asSequence().map {
            pages.get(it).asJsonObject
        }
    }

}
