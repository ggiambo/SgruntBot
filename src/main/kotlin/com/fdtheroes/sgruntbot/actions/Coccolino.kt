package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.Users
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Coccolino(private val sgruntBot: SgruntBot) : Action {

    private val regex = Regex("coccol(o|ino)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val user = Users.byId(message.from.id)
            if (user == Users.SUORA) {
                sgruntBot.rispondi(message, "Non chiamarmi così davanti a tutti!")
            }
        }
    }
}
