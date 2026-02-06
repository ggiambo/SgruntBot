package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.ScommessinaService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Scommessina(botUtils: BotUtils, botConfig: BotConfig, private val scommessinaService: ScommessinaService) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!scommessina(.+)?$")

    override fun handle(message: Message) {
        val groupValues = regex.find(message.text)?.groupValues
        if (groupValues.isNullOrEmpty()) {
            return
        }

        val termini = groupValues.getOrElse(1) { "" }
        if (termini.trim().isNotEmpty()) {
            createScommessima(message, termini.trim())
            return
        }

        if (message.replyToMessage != null) {
            acceptScommessima(message)
            return
        }

        botUtils.rispondi(ActionResponse.message("Qualcosa è andato storto, riprova e controlla"), message)
    }


    private fun createScommessima(message: Message, termini: String) {
        scommessinaService.createScommessina(message, termini)
        botUtils.rispondi(
            ActionResponse.message("🚀 Scommessina creata!\nPer partecipare rispondi con <b>!scommessina</b> al messaggio originale."),
            message
        )
    }

    private fun acceptScommessima(message: Message) {
        val rispondi = { messaggio: String -> botUtils.rispondi(ActionResponse.message(messaggio), message) }
        scommessinaService.addPartecipant(message, rispondi)
    }

    override fun halp(): String {
        return "<b>!scommessina</b> <i>termini della scommessa</i> crea una nuova scommessina."
    }
}
