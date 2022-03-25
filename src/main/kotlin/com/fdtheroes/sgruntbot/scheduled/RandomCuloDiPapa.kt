package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Service
class RandomCuloDiPapa(
    sgruntBot: SgruntBot,
    botUtils: BotUtils,
) : RandomScheduledAction(sgruntBot, botUtils) {

    override fun getMessageText(): String {
        val html = botUtils.textFromURL("https://dailyverses.net/it/versetto-casuale-bibbia")

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

        return "${testo}\n${autore}"
    }

}
