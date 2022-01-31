package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import org.jsoup.Jsoup
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RandomCuloDiPapa(sendMessage: (SendMessage) -> Unit) : RandomScheduledAction(sendMessage) {

    override fun getMessageText(): String {
        val html = BotUtils.textFromURL("https://dailyverses.net/it/versetto-casuale-bibbia")

        val banfata = Jsoup.parse(html)
        val contenitore = banfata.body()
            .select("div.content")
            .select("div.left")
            .select("div.b1")

        val testo = contenitore
            .select("span.v1")
            .text()

        val autore = contenitore
            .select("div.vr")
            .select("a.vc")
            .text()

        println(testo)
        println(autore)

        return "${banfata}\n${autore}"
    }

}
