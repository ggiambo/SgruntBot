package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Coccolino : Action {

    private val regex = Regex("coccol(o|ino)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        if (regex.containsMatchIn(message.text)) {
            val user = BotUtils.instance.userIds[message.from.id]
            if (user == BotUtils.Users.SUORA) {
                BotUtils.instance.rispondi(message, "Non chiamarmi così davanti a tutti!")
            }
        }
    }
}