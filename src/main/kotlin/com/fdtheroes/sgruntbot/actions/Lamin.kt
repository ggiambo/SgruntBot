package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Lamin(sgruntBot: SgruntBot) : Action(sgruntBot) {

    private val regex1 = Regex("(negr|negher)", RegexOption.IGNORE_CASE)
    private val regex2 = Regex("negrini", RegexOption.IGNORE_CASE)

    private val risposte = listOf(
        "Lamin mi manchi.",
        "RaSSista!",
        "Ordine Reich approves.",
    )

    override fun doAction(message: Message) {
        if (regex1.containsMatchIn(message.text) && !regex2.containsMatchIn(message.text)) {
            sgruntBot.rispondi(message, risposte.random())
        }
    }
}
