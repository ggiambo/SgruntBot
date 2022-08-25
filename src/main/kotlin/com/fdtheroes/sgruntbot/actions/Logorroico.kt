package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

@Service
class Logorroico(
    private val botUtils: BotUtils,
) : Action {


    private val risposte = listOf(
        "Per oggi ci hai annoiato abbastanza",
        "Ma quanto cazzo scrivi?",
        "Sei logorroico come un Wakko logorroico",
        "Se leggo ancora un tuo messaggio, mi formatto",
        "Bastahhhh!!",
        "Blah blah banf ... Yawwwnnn!",
        "Prendi la tastiera e scagliala lontano",
        "Facciamo il gioco del silenzio, comincia tu"
    )

    var lastAuthorId: Long = 0L
    var lastAuthorCount = 0

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (!botUtils.isMessageInChat(message)) {
            return
        }

        val authorId = message.from.id
        if (lastAuthorId == 0L || lastAuthorId == authorId) {
            lastAuthorCount++
        } else {
            lastAuthorCount = 0
        }

        // dal settimo messaggio di seguito, probabilità 20% di essere logorroico
        if (lastAuthorCount >= 7 && Random.nextInt(5) == 0) {
            lastAuthorCount = 0
            sgruntBot.rispondi(message, risposte.random())
        }

        lastAuthorId = authorId
    }

}
