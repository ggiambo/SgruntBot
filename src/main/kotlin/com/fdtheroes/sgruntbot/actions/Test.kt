package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Test(private val botUtils: BotUtils) : Action, HasHalp {

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (message.text == "!test") {
            doNext(ActionResponse.message("${botUtils.getUserLink(message.from)}: toast <pre>test</pre>"))
        }
    }

    override fun halp() = "<b>!test</b> toast"

}
