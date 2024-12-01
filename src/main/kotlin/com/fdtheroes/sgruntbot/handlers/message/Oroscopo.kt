package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Oroscopo(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!oroscopo(.*)\$", RegexOption.IGNORE_CASE)
    private val baseUrl = "https://www.foxoroscopo.com/oroscopo-del-giorno-%s"
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
            val url = baseUrl.format(segnoPerUrl.replaceFirstChar { it.uppercase() })

            val (introduzione, professioneCarriera, amorePartnership, lotto) = Jsoup.parse(botUtils.textFromURL(url))
                .select(".blog-pragraph")[0]
                .select("p")
                .map { it.text() }
                .map { it.replace("\\\'", "'") }

            val testo = """
                <b>${segnoPerUrl.replaceFirstChar { it.uppercase() }}</b>
                
                $introduzione
                
                <b>Professione e carriera</b>
                
                $professioneCarriera
                
                <b>Amore e partnership</b>
                
                $amorePartnership
                
                <b>Numeri del lotto</b>
                
                $lotto
            """.trimIndent()

            botUtils.rispondi(ActionResponse.message(testo), message)
        }
    }


    override fun halp() = "<b>!oroscopo segno</b> per sapere quale sfiga si abbatterà su di te"
}