package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.springframework.stereotype.Service

@Service
class Utonti(
    private val botUtils: BotUtils,
    private val usersService: UsersService,
) : Action, HasHalp {

    private val regex = Regex("^!utonti$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.matches(ctx.message.text)) {
            val utonti = usersService.getAllUsers(ctx.getChatMember)
                .joinToString(separator = "\n") { "- ${it.id}: ${botUtils.getUserName(it)}" }
            ctx.addResponse(ActionResponse.message("Utonti di questa ciat:\n${utonti}"))
        }
    }

    override fun halp() = "<b>!utonti</b> lista degli IDs degli utonti che hanno partecipato a questa ciat"
}
