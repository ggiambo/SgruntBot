package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Lamin(private val sgruntBot: SgruntBot) : Action {

    private val regex1 = Regex("(negr|negher)", RegexOption.IGNORE_CASE)
    private val regex2 = Regex("negrini", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex1.containsMatchIn(message.text) && !regex2.containsMatchIn(message.text)) {
            sgruntBot.rispondi(message, "Lamin mi manchi.")
        }
    }
}
