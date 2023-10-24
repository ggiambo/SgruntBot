package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance

@ApplicationScoped
class Halp(private val halp: Instance<HasHalp>) : Action {

    private val regex = Regex("^!help", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text)) {
            val risposta = halp
                .sortedBy { it.javaClass.simpleName }
                .joinToString("\n") { it.halp() }
            ctx.addResponse(ActionResponse.message(risposta))
        }
    }
}
