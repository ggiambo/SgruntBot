package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Rogan : Action {

    private val regex = Regex("\\brogan\\b", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            doNext.rispondi(message, "Cheppalle! Yawn!")
        }
    }
}
