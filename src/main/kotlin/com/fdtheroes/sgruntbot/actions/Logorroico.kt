package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

@Service
class Logorroico(private val botConfig: BotConfig) : Action {

    private val log = LoggerFactory.getLogger(this.javaClass)

    var lastAuthorCount = 0
    private val risposte = listOf(
        "Per oggi ci hai annoiato abbastanza",
        "Ma quanto cazzo scrivi?",
        "Sei logorroico come un Wakko logorroico",
        "Se leggo ancora un tuo messaggio, mi formatto",
        "Bastahhhh!!",
        "Blah blah banf ... Yawwwnnn!"
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (botConfig.lastAuthor?.id == message.from.id) {
            lastAuthorCount++
            log.info("Dato che ${botConfig.lastAuthor?.id} == ${message.from.id}, incremento lastAuthorCount di uno")
        } else {
            log.info("lastAuthorCount torna a zero")
            lastAuthorCount = 0
        }
        log.info("lastAuthorCount == $lastAuthorCount")
        if (lastAuthorCount > 5 && Random.nextInt(10) == 0) {
            log.info("E l'utente si becca una risposta")
            lastAuthorCount = 0
            sgruntBot.rispondi(message, risposte.random())
        }
    }

}
