package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class BlahBlahBanf(private val statsService: StatsService) : Action {

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        val messaggi = statsService.getMessagesToday(message.from)
        if (messaggi == 50) {
            sgruntBot.rispondi(message, "Oggi hai già scritto 50 messaggi, vedi di darti una calmata.")
        }
        if (messaggi == 100) {
            sgruntBot.rispondi(message, "Cento messaggi! Altri 100 e scatta il ban.")
        }
        if (messaggi == 150) {
            sgruntBot.rispondi(message, "Ti rimangono 50 messaggi, poi MUTOH!")
        }
        if (messaggi >= 200) {
            sgruntBot.cancella(message)
        }
    }
}
