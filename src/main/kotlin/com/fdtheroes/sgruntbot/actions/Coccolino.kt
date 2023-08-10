package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Coccolino : Action {

    private val regex = Regex("coccol(o|ino)", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text)) {
            val user = Users.byId(ctx.message.from.id)
            if (user == Users.DANIELE) {
                ctx.addResponse(ActionResponse.message("Non chiamarmi così davanti a tutti!"))
            }
        }
    }
}
