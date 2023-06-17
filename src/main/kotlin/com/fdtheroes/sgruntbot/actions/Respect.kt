package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Respect(private val botUtils: BotUtils) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        val don = ctx.message.replyToMessage?.from
        if (ctx.message.text == "F" && don != null) {
            ctx.addResponse(ActionResponse.message("Baciamo le mani Don ${botUtils.getUserLink(don)}"))
        }
    }

    override fun halp() = "<b>F</b> baciamo le mani dell'autore!"
}
