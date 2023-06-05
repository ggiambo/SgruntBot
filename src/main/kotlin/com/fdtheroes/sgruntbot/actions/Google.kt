package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Google : Action, HasHalp {

    private val regex = Regex("^!google (.*)$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        val query = regex.find(ctx.message.text)?.groupValues?.get(1)
        if (query != null) {
            ctx.addResponse(ActionResponse.message("""Cercatelo con <a href="https://www.google.com/search?q=$query">google</a> ritardato!™"""))
        }
    }

    override fun halp() = "<b>!google</b> <i>termine da cercare</i>"
}
