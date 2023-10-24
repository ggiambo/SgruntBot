package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class Id : Action, HasHalp {

    private val regex = Regex("^!id$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.matches(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("Il tuo id: ${ctx.message.from.id}"))
        }
    }

    override fun halp() = "<b>!id</b> mostra il tuo id di Telegram"
}
