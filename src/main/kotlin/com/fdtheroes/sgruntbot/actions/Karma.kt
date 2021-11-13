package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

class Karma : Action {

    override fun doAction(message: Message) {
        val ricevente = message.replyToMessage?.from?.id
        if (message.text == "+" && ricevente != null) {
            giveKarma(message, ricevente)
        }
        if (message.text == "-" && ricevente != null) {
            takeKarma(message, ricevente)
        }
        if (message.text == "!karma") {
            BotUtils.rispondi(message, testoKarma())
        }
    }

    private fun giveKarma(message: Message, ricevente: Long) {
        val done = giveTakeKarma(message, ricevente, KarmaRepository::giveKarma)
        if (done && nextInt(3) > 1) { // 33%
            karmaRoulette(message, Int::inc)
        }
    }

    private fun takeKarma(message: Message, ricevente: Long) {
        val done = giveTakeKarma(message, ricevente, KarmaRepository::takeKarma)
        if (done && nextInt(3) > 0) { // 66%
            karmaRoulette(message, Int::dec)
        }
    }

    private fun giveTakeKarma(
        message: Message,
        ricevente: Long,
        takeGive: (donatore: Long, ricevente: Long) -> Unit
    ): Boolean {
        val donatore = message.from.id
        if (donatore == ricevente) {
            BotUtils.rispondi(message, "Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
            return false
        }

        KarmaRepository.precheck(donatore)
        KarmaRepository.precheck(ricevente)

        if (KarmaRepository.getKarmaCredit(donatore) < 1) {
            BotUtils.rispondi(message, "Hai terminato i crediti per oggi")
            return false
        }

        takeGive(donatore, ricevente)

        val riceventeLink = BotUtils.getUserLink(message.replyToMessage.from)
        val donatoreLink = BotUtils.getUserLink(message.from)
        val karma = KarmaRepository.getKarma(ricevente)
        val crediti = KarmaRepository.getKarmaCredit(donatore)
        BotUtils.rispondi(message, "Karma totale di $riceventeLink: $karma\nCrediti di $donatoreLink: $crediti")

        return true
    }

    private fun karmaRoulette(message: Message, takeGive: (Int) -> Int) {
        val donatore = message.from.id
        KarmaRepository.takeGiveKarma(donatore, takeGive)
        val karma = KarmaRepository.getKarma(donatore)
        BotUtils.rispondi(message, "<b>Karmaroulette</b> ! Il tuo Karma è ora di $karma")
    }

    companion object {

        fun testoKarma(): String {
            val karmas = KarmaRepository.getKarmas()
                .sortedByDescending { it.second }
                .map { "${getUserName(it.first).padEnd(20)} ${it.second}" }
                .joinToString("\n")
            return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
        }

        private fun getUserName(userId: Long) = BotUtils.getUserName(BotUtils.getChatMember(userId))
    }
}
