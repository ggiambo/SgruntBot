package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class Smetti(private val botConfig: BotConfig) : Action {

    private val regex = Regex("^@?(sgrunty?|BlahBanf)(bot)? .*smetti.*", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (regex.containsMatchIn(ctx.message.text)) {
            botConfig.pausedTime = LocalDateTime.now().plusMinutes(5)
            ctx.addResponse(ActionResponse.message("Ok, sto zitto 5 minuti. :("))
        }
    }
}
