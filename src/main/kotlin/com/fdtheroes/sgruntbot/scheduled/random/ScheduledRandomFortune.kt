package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.handlers.message.Fortune
import com.fdtheroes.sgruntbot.handlers.message.Vocale
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service

@Service
class ScheduledRandomFortune(
    private val botUtils: BotUtils,
    private val fortune: Fortune,
    private val vocale: Vocale,
) : ScheduledRandom {

    val citazione = Regex("^\\s+-- .+\$") // la citazione in fondo

    override fun execute() {
        botUtils.messaggio(ActionResponse.message(fortune.getFortune()))
    }

    /*
    override fun execute() {
        //botUtils.messaggio(ActionResponse.message(fortune.getFortune()))
        val vocale = vocale.getVocale(getTestoFortune())
        botUtils.messaggio(ActionResponse.audio("Fortune", vocale))
    }

    private fun getTestoFortune(): String {
        val testoFortune = fortune.getFortune()
            .replace(citazione, "")
            .trim()
        while (testoFortune.length > 100) {
            return getTestoFortune()
        }
        return testoFortune
    }
     */

}