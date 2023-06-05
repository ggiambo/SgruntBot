package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.urlEncode
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Wiki(private val botUtils: BotUtils, val mapper: ObjectMapper) : Action, HasHalp {

    private val regex = Regex("^!wiki (.*)$", RegexOption.IGNORE_CASE)

    private val URL_titleAndURL = "https://it.wikipedia.org/w/api.php?action=opensearch&profile=fuzzy&search=%s"
    private val URL_extract =
        "https://it.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=%s"

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        val query = regex.find(ctx.message.text)?.groupValues?.get(1)
        if (query != null) {
            val titleAndURL = getTitleAndURL(query.urlEncode())
            val title = titleAndURL.first
            val url = titleAndURL.second
            if (title.isNullOrEmpty() || url.isNullOrEmpty()) {
                ctx.addResponse(ActionResponse.message("Non c'è."))
                return
            }

            val testo = getExtract(title.urlEncode())
            if (testo.isNullOrEmpty()) {
                ctx.addResponse(ActionResponse.message("Non c'è."))
                return
            }

            val risposta = "$testo\n$url"
            ctx.addResponse(ActionResponse.message(risposta))
        }
    }

    override fun halp() = "<b>!wiki</b> <i>termine da cercare</i>"

    private fun getTitleAndURL(query: String): Pair<String?, String?> {
        val testo =
            botUtils.textFromURL(URL_titleAndURL, query)
        val jsNode = mapper.readTree(testo)

        val titolo = jsNode.get(1).get(0)?.textValue()
        val url = jsNode.get(3).get(0)?.textValue()

        return Pair(titolo, url)
    }

    private fun getExtract(title: String): String? {
        val testo =
            botUtils.textFromURL(URL_extract, title)
        val jsNode = mapper.readTree(testo)
            .get("query")
            .get("pages")
            .elements()
            .next()
            .get("extract")

        return jsNode?.textValue()
    }

}
