package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import org.telegram.telegrambots.meta.api.methods.ParseMode
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
            BotUtils.rispondi(message, testoKarma(), ParseMode.HTML)
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
            BotUtils.rispondi(message, "Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso")
            return
        }

        karmaRepository.precheck(donatore)
        karmaRepository.precheck(ricevente)

        if (karmaRepository.getKarmaCredit(donatore) < 1) {
            BotUtils.rispondi(message, "Hai terminato i crediti per oggi")
            return
        }

        takeGive(donatore, ricevente)

        val riceventeLink = BotUtils.getUserLink(message.replyToMessage?.from)
        val karma = karmaRepository.getKarma(ricevente)
        BotUtils.rispondi(message, "Karma totale di $riceventeLink: $karma")
    }

    companion object {

        fun testoKarma(): String {
            val karmas = KarmaRepository().getKarmas()
                .sortedByDescending { it.second }
                .map { "${getUserName(it.first).padEnd(20)} ${it.second}" }
                .joinToString("\n")
            return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
        }

        private fun getUserName(userId : Long) = BotUtils.getUserName(BotUtils.getChatMember(userId))
    }
}
