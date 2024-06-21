package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service

@Service
class ScheduledRandomCuloDiPapa(private val botUtils: BotUtils) : ScheduledRandom {

    override fun execute() {
        val html = botUtils.textFromURL("https://dailyverses.net/it/versetto-casuale-bibbia")

        val banfata = Jsoup.parse(html)
        val contenitore = banfata.body()
            .select("div.content")
            .select("div.b2")
            .select("div.b2")
            .select("div.b2")

        val testo = contenitore
            .select("span.v2")
            .text()

        val autore = contenitore
            .select("div.vr")
            .select("a.vc")
            .text()

        val text = "${testo}\n${autore}"

        botUtils.messaggio(ActionResponse.message(text))
    }

}
