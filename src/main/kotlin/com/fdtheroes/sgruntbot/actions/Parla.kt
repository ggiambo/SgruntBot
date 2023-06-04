package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Parla(private val botConfig: BotConfig) : Action, HasHalp {

    private val regex = Regex(
        "^!parla (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        val msg = regex.find(message.text)?.groupValues?.get(1)
        if (msg != null) {
            doNext(ActionResponse.message("Mi dicono di dire: $msg"))
        }
    }

    override fun halp() = "<b>!parla</b> <i>testo</i> fai dire qualcosa a Sgrunty"
}
