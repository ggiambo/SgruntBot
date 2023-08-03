package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiService
import org.springframework.stereotype.Service

@Service
class Attacca(
    private val errePiGiService: ErrePiGiService
) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        val message = ctx.message
        if (message.text == "!attacca") {
            if (message.replyToMessage == null) {
                ctx.addResponse(ActionResponse.message("E chi vorresti mai attaccare, grullo?"))
                return
            }

            val attacco = errePiGiService.attacca(message.from, message.replyToMessage.from)
            ctx.addResponse(ActionResponse.message(attacco))
        }
    }

    override fun halp() =
        "<b>!attacca</b> Rispondi a un messaggio con questo testo per attaccare l'autore"

}
