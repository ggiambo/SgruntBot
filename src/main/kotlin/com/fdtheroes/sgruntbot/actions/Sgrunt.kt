package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Sgrunt : Action {

    private val regex = Regex("^sgrunt(bot|y|olino|olomeo)", RegexOption.IGNORE_CASE)
    private val reply = listOf(
        "Cazzo vuoi!?!",
        "Chi mi chiama?",
        "E io che c'entro adesso?",
        "Farò finta di non aver sentito",
        "Sgru' che... smuà!"
    )

    override fun doAction(message: Message, context: Context) {
        if (regex.containsMatchIn(message.text)) {
            val user = BotUtils.instance.userIds[message.from.id]
            if (user == BotUtils.Users.SUORA) {
                BotUtils.instance.rispondi(message, "Ciao papà!")
            } else {
                BotUtils.instance.rispondi(message, reply.random())
            }
        }
    }
}