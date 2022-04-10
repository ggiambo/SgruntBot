package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.Users
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime

@Service
class Smetti : Action {

    private val regex = Regex("^@?sgrunt(y|bot) .*smetti.*", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            val user = Users.byId(message.from.id)
            if (user == Users.DADA) {
                sgruntBot.rispondi(message, "Col cazzo!")
            } else {
                Context.pausedTime = LocalDateTime.now().plusMinutes(5)
                sgruntBot.rispondi(message, "Ok, sto zitto 5 minuti. :(")
            }
        }
    }
}
