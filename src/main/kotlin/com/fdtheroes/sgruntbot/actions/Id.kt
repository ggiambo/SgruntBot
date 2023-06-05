package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Id : Action, HasHalp {

    private val regex = Regex("^!id$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (regex.matches(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("Il tuo id: ${ctx.message.from.id}"))
        }
    }

    override fun halp() = "<b>!id</b> mostra il tuo id di Telegram"
}
