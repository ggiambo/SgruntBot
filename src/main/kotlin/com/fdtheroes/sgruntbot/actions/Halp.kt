package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Halp(private val halp: List<HasHalp>) : Action {

    private val regex = Regex("!help", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            val risposta = halp
                .sortedBy { it.javaClass.simpleName }
                .joinToString("\n") { it.halp() }
            doNext(ActionResponse.message(risposta))
        }
    }
}