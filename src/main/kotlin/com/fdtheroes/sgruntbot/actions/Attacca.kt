package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiService
import com.fdtheroes.sgruntbot.actions.persistence.UsersService

//@Service
class Attacca(
    private val errePiGiService: ErrePiGiService,
    private val usersService: UsersService,
    private val botUtils: BotUtils,
) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        val message = ctx.message
        if (message.text == "!attacca") {
            val testo: String
            if (message.replyToMessage == null) {
                testo = "E chi vorresti mai attaccare, grullo?"
            } else {
                testo = errePiGiService.attacca(message.from, message.replyToMessage.from, ctx.getChatMember)
            }
            ctx.addResponse(ActionResponse.message(testo))
        }
        if (message.text == "?attacca") {
            val difensore = usersService
                .getAllUsers { ctx.getChatMember(it) }
                .random()

            val testoAttacco = errePiGiService.attacca(message.from, difensore, ctx.getChatMember)
            val testo = "${botUtils.getUserName(message.from)} ubriaco fradicio tenta di attaccare ${
                botUtils.getUserName(difensore)
            }"
            ctx.addResponse(ActionResponse.message("$testo\n$testoAttacco"))
        }
    }

    override fun halp() =
        "<b>!attacca</b> Rispondi a un messaggio con questo testo per attaccare l'autore\n<b>?attacca</b> Attacco random."

}
