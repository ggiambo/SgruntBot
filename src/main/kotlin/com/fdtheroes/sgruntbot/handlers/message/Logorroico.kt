package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random.Default.nextInt

//@Service
class Logorroico(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

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

    private var lastAuthorId: Long = 0L
    private var lastAuthorCount = 0

    override fun handle(message: Message) {
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
        if (lastAuthorCount >= 10 && nextInt(5) == 0) {
            lastAuthorCount = 0
            botUtils.rispondi(ActionResponse.message(risposte.random()), message)
        }

        lastAuthorId = authorId
    }

}
