package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Test(botUtils: BotUtils) : Action(botUtils) {

    override fun doAction(message: Message, context: Context) {
        if (message.text == "!test") {
            botUtils.rispondi(message, "${botUtils.getUserLink(message)}: toast `test`")
        }
    }
}