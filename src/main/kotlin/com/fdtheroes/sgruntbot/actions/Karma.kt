package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

class Karma : Action, HasHalp {

    override fun doAction(message: Message) {
        val ricevente = message.replyToMessage?.from?.id
        if (message.text == "+" && ricevente != null) {
            giveTakeKarma(message, ricevente, Int::inc)
        }
        if (message.text == "-" && ricevente != null) {
            giveTakeKarma(message, ricevente, Int::dec)
        }
        if (message.text == "!karma") {
            BotUtils.rispondi(message, testoKarmaReport())
        }
    }

    override fun halp() = """
        <b>!karma</b> mostra la situazione del Karma
        <b>+</b> da un punto karma all'autore del messaggio
        <b>-</b> togle punto karma all'autore del messaggio
        """.trimIndent()

    private fun giveTakeKarma(message: Message, ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val donatore = message.from.id
        if (donatore == ricevente) {
            BotUtils.rispondi(message, "Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
            return
        }

        KarmaRepository.precheck(donatore)
        KarmaRepository.precheck(ricevente)

        if (KarmaRepository.getKarmaCredit(donatore) < 1) {
            BotUtils.rispondi(message, "Hai terminato i crediti per oggi")
            return
        }

        KarmaRepository.takeGiveKarma(donatore, ricevente, newKarma)

        val riceventeLink = BotUtils.getUserLink(message.replyToMessage.from)
        val donatoreLink = BotUtils.getUserLink(message.from)
        val karma = KarmaRepository.getKarma(ricevente)
        val crediti = KarmaRepository.getKarmaCredit(donatore)
        var karmaMessage = "Karma totale di $riceventeLink: $karma\nCrediti di $donatoreLink: $crediti"

        if (nextInt(5) == 0) { // 20%
            val karmaRoulette = karmaRoulette(message, newKarma)
            karmaMessage = karmaMessage.plus("\n\n$karmaRoulette")
        }

        BotUtils.rispondi(message, karmaMessage)
    }

    private fun karmaRoulette(message: Message, newKarma: (oldKarma: Int) -> Int): String {
        val ricevente = message.from.id
        KarmaRepository.takeGiveKarma(ricevente, newKarma)
        val karma = KarmaRepository.getKarma(ricevente)
        return "<b>Karmaroulette</b> ! Il tuo Karma è ora di $karma"
    }

    companion object {

        fun testoKarmaReport(): String {
            val karmas = KarmaRepository.getKarmas()
                .sortedByDescending { it.second }
                .map { "${getUserName(it.first).padEnd(20)}%3d".format(it.second) }
                .joinToString("\n")
            return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
        }

        private fun getUserName(userId: Long) = BotUtils.getUserName(BotUtils.getChatMember(userId))
    }
}
