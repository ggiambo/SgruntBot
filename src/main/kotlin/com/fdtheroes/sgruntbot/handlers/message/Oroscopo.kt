package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Oroscopo(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!oroscopo(.*)\$", RegexOption.IGNORE_CASE)
    private val baseUrl = "https://www.oggi.it/oroscopo/oroscopo-di-oggi/%s-oggi.shtml"
    private val segni = setOf(
        "ariete",
        "toro",
        "gemelli",
        "cancro",
        "leone",
        "vergine",
        "bilancia",
        "scorpione",
        "sagittario",
        "capricorno",
        "acquario",
        "pesci"
    )

    override fun handle(message: Message) {
        if (regex.matches(message.text)) {
            val segno = regex.find(message.text)?.groupValues?.get(1)?.trim()?.lowercase()

            val segnoPerUrl = if (segno.isNullOrEmpty() || !segni.contains(segno)) {
                segni.random()
            } else {
                segno
            }
            val url = baseUrl.format(segnoPerUrl.apply { replaceFirstChar { it.uppercase() } })

            val (introduzione, amore, lavoro, benessere) = Jsoup.parse(botUtils.textFromURL(url))
                .select("Article.oroscopo")
                .select("p")
                .filter { it: Element -> it.text().isNotBlank() }

            val testo = """
                ${introduzione.text()}\n
                <b>Amore e eros</b>${amore.ownText()}\n
                <b>Lavoro e denaro</b>${lavoro.ownText()}\n
                <b>Benessere</b>${benessere.ownText()}\n
            """.trimIndent()

            botUtils.rispondi(ActionResponse.message(testo), message)
        }
    }


    override fun halp() = "<b>!oroscopo segno</b> per sapere quale sfiga si abbatterà su di te"
}