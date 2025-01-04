package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextLong

//@Service
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

    // random tra 12 e 16 ore
    override fun firstRun(): LocalDateTime {
        val minuti = nextLong(12 * 60, 16 * 60)
        return LocalDateTime.now().plusMinutes(minuti)
    }


    // random tra 16 e 24 ore
    override fun nextRun(): LocalDateTime {
        val minuti = nextLong(16 * 60, 24 * 60)
        return LocalDateTime.now().plusMinutes(minuti)
    }

}
