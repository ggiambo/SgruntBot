package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Test : Action, HasHalp {

    override fun doAction(message: Message) {
        if (message.text == "!test") {
            BotUtils.rispondi(message, "${BotUtils.getUserLink(message.from)}: toast <pre>test</pre>")
        }
    }

    override fun halp() = "<b>!test</b> toast"

}
