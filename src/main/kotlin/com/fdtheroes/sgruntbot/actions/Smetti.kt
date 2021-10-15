package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime

class Smetti : Action {

    private val regex = Regex("^@?sgrunt(y|bot) .*smetti.*", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        if (regex.containsMatchIn(message.text)) {
            val user = BotUtils.instance.userIds[message.from.id]
            if (user == BotUtils.Users.DADA) {
                BotUtils.instance.rispondi(message, "Col cazzo!")
            } else {
                context.pausedTime = LocalDateTime.now()
                BotUtils.instance.rispondi(message, "Ok, sto zitto 5 minuti. :(")
            }
        }
    }
}