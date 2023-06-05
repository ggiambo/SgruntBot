package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service

@Service
class Sgrunt : Action {

    private val regex = Regex("^sgrunt(bot|y|olino|olomeo)", RegexOption.IGNORE_CASE)
    private val reply = listOf(
        "Cazzo vuoi!?!",
        "Chi mi chiama?",
        "E io che c'entro adesso?",
        "Farò finta di non aver sentito",
        "Sgru' che... smuà!"
    )

    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (regex.containsMatchIn(ctx.message.text)) {
            val user = Users.byId(ctx.message.from.id)
            if (user == Users.SUORA) {
                ctx.addResponse(ActionResponse.message("Ciao papà!"))
            } else {
                ctx.addResponse(ActionResponse.message(reply.random()))
            }
        }
        doNextAction()
    }
}
