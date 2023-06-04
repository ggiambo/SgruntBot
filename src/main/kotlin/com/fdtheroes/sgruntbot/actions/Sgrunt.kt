package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

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

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            val user = Users.byId(message.from.id)
            if (user == Users.SUORA) {
                doNext(ActionResponse.message("Ciao papà!"))
            } else {
                doNext(ActionResponse.message(reply.random()))
            }
        }
    }
}
