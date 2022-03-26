package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class PorcoDio : Action {

    private val regex = Regex("\\bporco dio\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (Context.pignolo && regex.containsMatchIn(message.text)) {
            sgruntBot.rispondi(message, "E la madooonna!")
        }
    }

}
