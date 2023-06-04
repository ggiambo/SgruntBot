package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Lamin : Action {

    private val regex1 = Regex("(negr|negher)", RegexOption.IGNORE_CASE)
    private val regex2 = Regex("negrini", RegexOption.IGNORE_CASE)

    private val risposte = listOf(
        "Lamin mi manchi.",
        "RaSSista!",
        "Ordine Reich approves.",
    )

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex1.containsMatchIn(message.text) && !regex2.containsMatchIn(message.text)) {
            doNext(ActionResponse.message(risposte.random()))
        }
    }
}
