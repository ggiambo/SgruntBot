package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Lamin : Action {

    private val regex1 = Regex("(negr|negher)", RegexOption.IGNORE_CASE)
    private val regex2 = Regex("negrini", RegexOption.IGNORE_CASE)

    private val risposte = listOf(
        "Lamin mi manchi.",
        "RaSSista!",
        "Ordine Reich approves.",
    )

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (regex1.containsMatchIn(ctx.message.text) && !regex2.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message(risposte.random()))
        }
        doNextAction()
    }
}
