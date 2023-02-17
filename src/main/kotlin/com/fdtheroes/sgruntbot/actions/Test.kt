package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Test(sgruntBot: SgruntBot, private val botUtils: BotUtils) : Action(sgruntBot), HasHalp {

    override fun doAction(message: Message) {
        if (message.text == "!test") {
            sgruntBot.rispondi(message, "${botUtils.getUserLink(message.from)}: toast <pre>test</pre>")
        }
    }

    override fun halp() = "<b>!test</b> toast"

}
