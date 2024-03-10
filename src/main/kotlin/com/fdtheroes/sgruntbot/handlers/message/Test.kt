package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Test(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    override fun handle(message: Message) {
        if (message.text == "!test") {
            botUtils.rispondi(
                ActionResponse.message("${botUtils.getUserLink(message.from)}: toast <pre>test</pre>"),
                message
            )
        }
    }

    override fun halp() = "<b>!test</b> toast"

}
