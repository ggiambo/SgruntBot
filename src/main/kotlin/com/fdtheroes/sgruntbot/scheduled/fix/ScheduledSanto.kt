package com.fdtheroes.sgruntbot.scheduled.fix

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.springframework.web.util.HtmlUtils
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.time.LocalDateTime

@Service
class ScheduledSanto(
    private val botUtils: BotUtils,
    private val mapper: ObjectMapper,
) : Scheduled {

    override fun firstRun() = seiDiMattina()

    override fun nextRun() = seiDiMattina()

    override fun execute() {
        val santi = botUtils.textFromURL("https://www.santodelgiorno.it/santi.json")
        val jsNode = mapper.readTree(santi)

        santoDiDefault(jsNode)
    }

    private fun santoDiDefault(jsNode: JsonNode) {
        val santo = jsNode.firstOrNull { it["default"].asInt() == 1 }

        if (santo == null) {
            botUtils.messaggio(ActionResponse.message("Niente santo del giorno oggi"))
            return
        }

        val nome = HtmlUtils.htmlUnescape(santo["nome"].asText())
        val descrizione = HtmlUtils.htmlUnescape(santo["descrizione"].asText())
        val url = santo["permalink"].asText()
        val urlPhoto = santo["urlimmagine"].asText()

        val imageStream = botUtils.streamFromURL(urlPhoto)
        val inputPhoto = InputFile(imageStream, "santo.jpg")

        botUtils.messaggio(ActionResponse.photo("<a href='$url'>$nome</a>\n$descrizione", inputPhoto))
    }

    private fun seiDiMattina(): LocalDateTime {
        val seiDiMattina = LocalDateTime.now()
            .withHour(6)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        return seiDiMattina.plusDays(1)
    }

}
