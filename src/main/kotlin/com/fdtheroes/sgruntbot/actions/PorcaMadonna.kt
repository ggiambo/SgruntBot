package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse

//@Service
class PorcaMadonna(private val botConfig: BotConfig) : Action {

    private val regex = Regex("\\bporca madonna\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(ctx: ActionContext) {
        if (botConfig.pignolo && regex.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("...e tutti gli angeli in colonna!"))
        }
    }

}
