package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Test : Action {

    override fun doAction(message: Message, context: Context) {
        if (message.text == "!test") {
            BotUtils.instance.rispondi(message, "${BotUtils.instance.getUserLink(message)}: toast `test`")
        }
    }
}