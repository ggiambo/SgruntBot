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
            val testo: String
            if (message.replyToMessage == null) {
                testo = getErrePiGiReport(ctx)
            } else {
                testo = errePiGiService.attacca(message.from, message.replyToMessage.from)
            }
            ctx.addResponse(ActionResponse.message(testo))
        }
    }

    private fun getErrePiGiReport(ctx: ActionContext): String {
        val testoErrePiGiReport = errePiGiService.testoErrePiGiReport(ctx.getChatMember)
        if (testoErrePiGiReport == null) {
            return "E chi vorresti mai attaccare, grullo?"
        }
        return "E chi vorresti mai attaccare, grullo?\n\n$testoErrePiGiReport"
    }

    override fun halp() = "<b>!attacca</b> Rispondi a un messaggio con questo testo per attaccare l'autore"

}
