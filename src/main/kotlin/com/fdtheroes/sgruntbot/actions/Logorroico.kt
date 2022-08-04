package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

@Service
class Logorroico(
    private val botUtils: BotUtils,
) : Action {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val risposte = listOf(
        "Per oggi ci hai annoiato abbastanza",
        "Ma quanto cazzo scrivi?",
        "Sei logorroico come un Wakko logorroico",
        "Se leggo ancora un tuo messaggio, mi formatto",
        "Bastahhhh!!",
        "Blah blah banf ... Yawwwnnn!"
    )

    var lastAuthorId : Long = 0
    var lastAuthorCount = 0

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (!botUtils.isMessageInChat(message)) {
            return
        }

        val authorId = message.from.id

        if (lastAuthorId == authorId) {
            lastAuthorCount++
            log.info("Dato che $lastAuthorId == $authorId, incremento lastAuthorCount di uno")
        } else {
            log.info("lastAuthorCount torna a zero")
            lastAuthorCount = 0
        }

        log.info("lastAuthorCount == $lastAuthorCount")

        // dal quinto messaggio di seguito, probabilità 20% di essere logorroico
        if (lastAuthorCount > 5 && Random.nextInt(5) == 0) {
            log.info("E l'utente si becca una risposta")
            lastAuthorCount = 0
            sgruntBot.rispondi(message, risposte.random())
        }

        lastAuthorId = authorId
    }

}
