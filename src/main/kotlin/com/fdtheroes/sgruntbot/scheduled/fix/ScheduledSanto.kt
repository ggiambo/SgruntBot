package com.fdtheroes.sgruntbot.scheduled.fix

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.time.LocalDateTime

@Service
class ScheduledSanto(
    private val botUtils: BotUtils,
    private val mapper: ObjectMapper,
    private val sgruntBot: Bot,
) : ScheduledAMezzanotte {

    override fun firstRun(): LocalDateTime {
        val noveDiMattina = LocalDateTime.now()
            .withHour(9)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        return noveDiMattina.plusDays(1)
    }

    override fun execute() {
        val santi = botUtils.textFromURL("https://www.santodelgiorno.it/santi.json")
        val jsNode = mapper.readTree(santi)
        val santo = jsNode.firstOrNull { it.get("default").asInt() == 1 }

        if (santo == null) {
            sgruntBot.messaggio(ActionResponse.message("Niente santo del giorno oggi"))
            return
        }

        val nome = santo.get("nome").asText()
        val descrizione = santo.get("descrizione").asText()
        val url = santo.get("permalink").asText()
        val urlPhoto = santo.get("urlimmagine").asText()

        val imageStream = botUtils.streamFromURL(urlPhoto)
        val inputPhoto = InputFile(imageStream, "santo.jpg")

        sgruntBot.messaggio(ActionResponse.photo("<a href='$url'>$nome</a>\n$descrizione", inputPhoto, false))
    }

}
