package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.urlEncode
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.net.URLEncoder

@Service
class Wiki(private val botUtils: BotUtils, val mapper: ObjectMapper) : Action, HasHalp {

    private val regex = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        val query = regex.find(message.text)?.groupValues?.get(1)
        if (query != null) {
            val titleAndURL = getTitleAndURL(query.urlEncode())
            val title = titleAndURL.first
            val url = titleAndURL.second
            if (title.isNullOrEmpty() || url.isNullOrEmpty()) {
                return sgruntBot.rispondi(message, "Non c'è.")
            }

            val testo = getExtract(title.urlEncode())
            if (testo.isNullOrEmpty()) {
                return sgruntBot.rispondi(message, "Non c'è.")
            }

            val risposta = "$testo\n$url"
            sgruntBot.rispondi(message, risposta)
        }

    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i>"

    private fun getTitleAndURL(query: String): Pair<String?, String?> {
        val testo =
            botUtils.textFromURL("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=$query")
        val jsNode = mapper.readTree(testo)
        val titolo = jsNode.get(1).get(0)?.textValue()
        val url = jsNode.get(3).get(0)?.textValue()
        return Pair(titolo, url)
    }

    private fun getExtract(title: String): String? {
        val url =
            botUtils.textFromURL("https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=$title")

        return mapper.readTree(url)
            .get("query")
            .get("pages")
            .elements()
            .next()
            .get("extract")?.textValue()
    }

}

fun main() {

    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("127.0.0.1", 8888))

    val textFromUrl = { query: String ->
        URL("https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=${URLEncoder.encode(query)}")
            .openConnection(proxy)
            .getInputStream()
            .readAllBytes()
            .decodeToString()
    }

    val barzuffo = ObjectMapper().readTree(textFromUrl("barzuffo"))
    println(barzuffo)

    val fosse = ObjectMapper().readTree(textFromUrl("fosse ardeatine"))
    println(fosse)

    val giambo = ObjectMapper().readTree(textFromUrl("giambo"))
    println(giambo)

    val titoloAndURL = { jsNode: JsonNode ->
        val titolo = jsNode.get(1).get(0)?.asText()
        val url = jsNode.get(3).get(0)?.asText()
        Pair(titolo, url)
    }

    println(titoloAndURL(barzuffo))
    println(titoloAndURL(fosse))
    println(titoloAndURL(giambo))

    /*
    val res = obj.get(1)
        .get(0).asText()

    println(res)

     */
}
