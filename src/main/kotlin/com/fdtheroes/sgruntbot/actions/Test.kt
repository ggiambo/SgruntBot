package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Test : Action {

    override fun doAction(message: Message) {
        if (message.text == "!test") {
            BotUtils.rispondi(message, "${BotUtils.getUserLink(message.from)}: toast <pre>test</pre>")
        }
    }
}