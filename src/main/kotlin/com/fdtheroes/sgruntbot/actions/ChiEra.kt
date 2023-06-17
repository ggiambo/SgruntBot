package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class ChiEra(private val botUtils: BotUtils, private val botConfig: BotConfig) : Action, HasHalp {

    private val regex = Regex(
        "^!chiera$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text) && botConfig.lastSuper != null) {
            ctx.addResponse(ActionResponse.message(botUtils.getUserLink(botConfig.lastSuper)))
        }
    }

    override fun halp() = "<b>!chiera</b> ti dice chi ha usato per ultimo il comando !super"
}
