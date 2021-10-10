package com.fdtheroes.sgruntbot.utils

import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class WikiUtils {

    fun wikiSearch(query: String?, rispondi: (String) -> Unit) {
        var url = URL("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=$query")
            .openConnection()
            .getInputStream()
            .readAllBytes()
            .decodeToString()

        val first = JSONArray(url).getJSONArray(1).optString(0)
        if (first.isEmpty()) {
            return rispondi("Non c'è.")
        }

        url =
            URL("https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=$first")
                .openConnection()
                .getInputStream()
                .readAllBytes()
                .decodeToString()
        val pages = JSONObject(url).getJSONObject("query").getJSONObject("pages")
        pages.keys().forEach {
            val page = pages.getJSONObject(it)
            val testo = page.getString("extract")
            val title = page.getString("title")
            val risposta = "$testo\nhttps://it.wikipedia.org/wiki/$title"
            rispondi(risposta)
        }

    }
}