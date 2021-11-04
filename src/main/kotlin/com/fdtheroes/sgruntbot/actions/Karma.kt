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
        if (message.text == "!karma") {
            showKarma(message)
        }
    }

    private fun giveKarma(message: Message, ricevente: Long) {
        val donatore = message.from.id
        if (donatore == ricevente) {
            BotUtils.rispondi(message, "Autokarma è meschino!")
            return
        }

        karmaRepository.precheck(donatore)
        karmaRepository.precheck(ricevente)

        if (karmaRepository.getKarmaCredit(donatore) < 1) {
            BotUtils.rispondi(message, "Hai terminato i crediti per oggi")
            return
        }

        karmaRepository.giveKarma(donatore, ricevente)

        val riceventeLink = BotUtils.getUserLink(message.replyToMessage)
        val karma = karmaRepository.getKarma(ricevente)
        BotUtils.rispondi(message, "Karma totale di $riceventeLink: $karma")
    }

    private fun showKarma(message: Message) {
        val donatore = message.from.id
        karmaRepository.precheck(donatore)

        val donatoreLink = BotUtils.getUserLink(message)
        val karma = karmaRepository.getKarma(donatore)

        BotUtils.rispondi(message, "$donatoreLink ha $karma punti karma")
    }

}