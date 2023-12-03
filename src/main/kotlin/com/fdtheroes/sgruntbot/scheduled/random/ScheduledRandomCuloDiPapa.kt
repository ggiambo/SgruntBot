package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.jsoup.Jsoup

//@Service
class ScheduledRandomCuloDiPapa(private val botUtils: BotUtils, private val sgruntBot: Bot) : ScheduledRandom {

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

        sgruntBot.messaggio(ActionResponse.message(text, false))
    }

}
