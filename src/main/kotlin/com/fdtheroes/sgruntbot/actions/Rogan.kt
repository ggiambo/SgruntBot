package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Rogan : Action {

    private val regex = Regex("\\brogan\\b", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (regex.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("Cheppalle! Yawn!"))
        }
    }
}
