package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class ParlaSuper(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex(
        "^!parlasuper (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun handle(message: Message) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null && Users.byId(message.from.id) != null) {
            botUtils.messaggio(ActionResponse.message(testo))
            botConfig.lastSuper = message.from
        }
    }

    override fun halp() =
        "<b>!parlasuper</b> <i>testo</i> fai dire qualcosa a Sgrunty. Da usare in una chat privata con lui."
}
