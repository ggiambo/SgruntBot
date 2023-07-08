package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class PorcoDio(private val botConfig: BotConfig) : Action {

    private val regex = Regex("\\bporco dio\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(ctx: ActionContext) {
        if (botConfig.pignolo && regex.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message("E la madooonna!"))
        }
    }

}
