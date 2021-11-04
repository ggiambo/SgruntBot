package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import org.telegram.telegrambots.meta.api.objects.Message

class Karma : Action {

    val karmaRepository = KarmaRepository()

    override fun doAction(message: Message) {
        val ricevente = message.replyToMessage?.from?.id
        if (message.text == "+" && ricevente != null) {
            giveKarma(message, ricevente)
        }
        if (message.text == "-" && ricevente != null) {
            takeKarma(message, ricevente)
        }
        if (message.text == "!karma") {
            showKarma(message)
        }
    }

    private fun giveKarma(message: Message, ricevente: Long) {
        giveTakeKarma(message, ricevente, karmaRepository::giveKarma)
    }

    private fun takeKarma(message: Message, ricevente: Long) {
        giveTakeKarma(message, ricevente, karmaRepository::takeKarma)
    }

    private fun giveTakeKarma(message: Message, ricevente: Long, takeGive: (donatore: Long, ricevente: Long) -> Unit) {
        val donatore = message.from.id
        if (donatore == ricevente) {
            BotUtils.rispondi(message, "Hai il potere di dare o togliere a altri, non a te stesso (Taddeo, 12.7:4)")
            return
        }

        karmaRepository.precheck(donatore)
        karmaRepository.precheck(ricevente)

        if (karmaRepository.getKarmaCredit(donatore) < 1) {
            BotUtils.rispondi(message, "Hai terminato i crediti per oggi")
            return
        }

        takeGive(donatore, ricevente)

        val riceventeLink = BotUtils.getUserLink(message.replyToMessage)
        val karma = karmaRepository.getKarma(ricevente)
        BotUtils.rispondi(message, "Karma totale di $riceventeLink: $karma")
    }

    private fun showKarma(message: Message) {
        val userId = message.from.id
        karmaRepository.precheck(userId)

        val userLink = BotUtils.getUserLink(message)
        val karma = karmaRepository.getKarma(userId)

        BotUtils.rispondi(message, "$userLink ha $karma punti karma")
    }

}