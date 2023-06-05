package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Last(private val slogan: Slogan, private val botConfig: BotConfig) : Action, HasHalp {

    private val regex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (regex.matches(ctx.message.text) && botConfig.lastAuthor != null) {
            ctx.addResponse(ActionResponse.message(slogan.fetchSlogan(botConfig.lastAuthor!!)))
        }
    }

    override fun halp() = "<b>!last</b> uno slogan per l'ultimo autore"

}
