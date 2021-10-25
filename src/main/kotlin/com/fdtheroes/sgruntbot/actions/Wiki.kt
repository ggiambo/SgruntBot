package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.json.JSONArray
import org.json.JSONObject
import org.telegram.telegrambots.meta.api.objects.Message

class Wiki : Action {

    private val regex = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            val first = getSearchResponse(query)
            if (first.isEmpty()) {
                return BotUtils.instance.rispondi(message, "Non c'è.")
            }

            getResponsePages(first).forEach {
                val testo = it.getString("extract")
                val title = it.getString("title")
                val risposta = "$testo\nhttps://it.wikipedia.org/wiki/$title"
                BotUtils.instance.rispondi(message, risposta)
            }
        }
    }

    private fun getSearchResponse(query: String): String {
        val url = BotUtils.instance.textFromURL("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=$query")
        return JSONArray(url).getJSONArray(1).optString(0)
    }

    private fun getResponsePages(titles: String): Sequence<JSONObject> {
        val url =
            BotUtils.instance.textFromURL("https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=$titles")

        val pages = JSONObject(url)
            .getJSONObject("query")
            .getJSONObject("pages")

        return pages.keys().asSequence().map {
            pages.getJSONObject(it)
        }
    }

}