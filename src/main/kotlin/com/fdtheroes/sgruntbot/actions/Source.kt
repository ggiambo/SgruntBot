package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Source : Action, HasHalp {

    private val regex = Regex("^!source\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("http://github.com/ggiambo/SgruntBot"))
        }
    }

    override fun halp() = "<b>!source</b> mostra il sorgente di Sgrunty"

}
