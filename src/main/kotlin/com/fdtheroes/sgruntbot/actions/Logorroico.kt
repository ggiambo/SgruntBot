package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

@Service
class Logorroico(private val botConfig: BotConfig) : Action {

    var lastAuthorCount = 0
    private val risposte = listOf(
        "Bravo, per oggi ci hai annoiato abbastanza",
        "Ma quanto cazzo scrivi?",
        "Sei logorroico come un Wakko logorroico",
        "Se leggo ancora un tuo messaggio, mi formatto"
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (botConfig.lastAuthor?.id == message.from.id) {
            lastAuthorCount++
        } else {
            lastAuthorCount = 0
        }
        if (lastAuthorCount > 5 && Random.nextInt(5) == 0) {
            sgruntBot.rispondi(message, risposte.random())
            lastAuthorCount = 0
        }
    }

}