package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Rogan(sgruntBot: SgruntBot) : Action(sgruntBot) {

    private val regex = Regex("\\brogan\\b", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            sgruntBot.rispondi(message, "Cheppalle! Yawn!")
        }
    }
}
