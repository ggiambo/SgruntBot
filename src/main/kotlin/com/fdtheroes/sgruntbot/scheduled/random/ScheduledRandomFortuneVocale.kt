package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.handlers.message.Fortune
import com.fdtheroes.sgruntbot.handlers.message.Vocale
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Disabled
import org.springframework.stereotype.Service

@Service
@Disabled
class ScheduledRandomFortuneVocale(
    private val botUtils: BotUtils,
    private val fortune: Fortune,
    private val vocale: Vocale,
) : ScheduledRandom {

    private val citazione = Regex("^\\s+-- .+$") // la citazione in fondo

    override fun execute() {
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

}