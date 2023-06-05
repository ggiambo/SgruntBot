package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class ParlaSuper(private val botConfig: BotConfig) : Action, HasHalp {

    private val regex = Regex(
        "^!parlasuper (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        val testo = regex.find(ctx.message.text)?.groupValues?.get(1)
        if (testo != null && Users.byId(ctx.message.from.id) != null) {
            ctx.addResponse(ActionResponse.message(testo))
            botConfig.lastSuper = ctx.message.from
        }
    }

    override fun halp() =
        "<b>!parlasuper</b> <i>testo</i> fai dire qualcosa a Sgrunty. Da usare in una chat privata con lui."
}
