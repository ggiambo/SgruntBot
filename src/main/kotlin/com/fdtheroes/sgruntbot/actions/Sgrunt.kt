package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random

@Service
class Sgrunt : Action {

    private val regex = Regex("^sgrunt(bot|y|olino|olomeo)", RegexOption.IGNORE_CASE)
    private val reply = listOf(
        "Cazzo vuoi!?!",
        "Chi mi chiama?",
        "E io che c'entro adesso?",
        "Farò finta di non aver sentito",
        "Sgru' che... smuà!",
        "Si, anche tu sei il mio preferito <3",
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            if (Random.nextInt(5) == 0) {
                sgruntBot.rispondi(message, reply.random())
            }
        }
    }
}
