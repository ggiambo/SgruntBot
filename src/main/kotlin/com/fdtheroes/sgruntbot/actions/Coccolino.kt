package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Users
import org.telegram.telegrambots.meta.api.objects.Message

class Coccolino : Action {

    private val regex = Regex("coccol(o|ino)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val user = Users.byId(message.from.id)
            if (user == Users.SUORA) {
                BotUtils.rispondi(message, "Non chiamarmi così davanti a tutti!")
            }
        }
    }
}