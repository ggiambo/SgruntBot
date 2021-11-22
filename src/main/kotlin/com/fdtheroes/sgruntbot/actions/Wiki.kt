package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.urlEncode
import org.json.JSONArray
import org.json.JSONObject
import org.telegram.telegrambots.meta.api.objects.Message

class Wiki : Action, HasHalp {

    private val regex = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            val first = getSearchResponse(query.urlEncode())
            if (first.isEmpty()) {
                return BotUtils.rispondi(message, "Non c'è.")
            }

            getResponsePages(first.urlEncode()).forEach {
                val testo = it.getString("extract")
                val title = it.getString("title")
                val risposta = "$testo\nhttps://it.wikipedia.org/wiki/${title.urlEncode()}"
                BotUtils.rispondi(message, risposta)
            }
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i>"

    private fun getSearchResponse(query: String): String {
        val url = BotUtils.textFromURL("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=$query")
        return JSONArray(url).getJSONArray(1).optString(0)
    }

    private fun getResponsePages(titles: String): Sequence<JSONObject> {
        val url =
            BotUtils.textFromURL("https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=$titles")

        val pages = JSONObject(url)
            .getJSONObject("query")
            .getJSONObject("pages")

        return pages.keys().asSequence().map {
            pages.getJSONObject(it)
        }
    }

}