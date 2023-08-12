package com.fdtheroes.sgruntbot.scheduled

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class ScheduledSanto(
    private val botUtils: BotUtils,
    private val mapper: ObjectMapper,
    private val sgruntBot: Bot,
) : Timer() {

    val noveDiMattina = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }.time

    @PostConstruct
    fun start() {
        val oneDayInMilleseconds = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        Timer().schedule(PublishSantoDelGiorno(), noveDiMattina, oneDayInMilleseconds)
    }

    inner class PublishSantoDelGiorno : TimerTask() {
        override fun run() {
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

            sgruntBot.messaggio(ActionResponse.photo("<a href='$url'>$nome</a>\n$descrizione", inputPhoto))
        }
    }

}
