package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class PorcaMadonna(private val botConfig: BotConfig) : Action {

    private val regex = Regex("\\bporca madonna\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (botConfig.pignolo && regex.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("...e tutti gli angeli in colonna!"))
        }
        doNextAction()
    }

}
