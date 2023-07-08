package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Test(private val botUtils: BotUtils) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        if (ctx.message.text == "!test") {
            ctx.addResponse(ActionResponse.message("${botUtils.getUserLink(ctx.message.from)}: toast <pre>test</pre>"))
        }
    }

    override fun halp() = "<b>!test</b> toast"

}
