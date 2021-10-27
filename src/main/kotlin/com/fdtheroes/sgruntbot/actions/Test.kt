package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Test : Action {

    override fun doAction(message: Message, context: Context) {
        if (message.text == "!test") {
            BotUtils.instance.rispondi(message, "${BotUtils.instance.getUserLink(message)}: toast `test`")
        }
    }
}