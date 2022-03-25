package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class PorcaMadonna(private val sgruntBot: SgruntBot) : Action {

    private val regex = Regex("\\bporca madonna\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(message: Message) {
        if (Context.pignolo && regex.containsMatchIn(message.text)) {
            sgruntBot.rispondi(message, "...e tutti gli angeli in colonna!")
        }
    }

}
