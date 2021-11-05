package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.Users
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime

class Smetti : Action {

    private val regex = Regex("^@?sgrunt(y|bot) .*smetti.*", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
         if (regex.containsMatchIn(message.text)) {
            val user = Users.byId(message.from.id)
            if (user == Users.DADA) {
                BotUtils.rispondi(message, "Col cazzo!")
            } else {
                Context.pausedTime = LocalDateTime.now()
                BotUtils.rispondi(message, "Ok, sto zitto 5 minuti. :(")
            }
        }
    }
}