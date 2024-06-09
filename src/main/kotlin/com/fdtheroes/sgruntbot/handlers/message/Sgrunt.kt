package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Sgrunt(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val regex = Regex("^sgrunt(bot|y|olino|olomeo)", RegexOption.IGNORE_CASE)
    private val reply = listOf(
        "Cazzo vuoi!?!",
        "Chi mi chiama?",
        "E io che c'entro adesso?",
        "Farò finta di non aver sentito",
        "Sgru' che... smuà!"
    )

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val user = Users.byId(message.from.id)
            if (user == Users.DANIELE) {
                botUtils.rispondi(ActionResponse.message("Ciao papà!"), message)
            } else {
                botUtils.rispondi(ActionResponse.message(reply.random()), message)
            }
        }
    }
}
