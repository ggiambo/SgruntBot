package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Last(private val slogan: Slogan, private val botConfig: BotConfig) : Action, HasHalp {

    private val regex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.matches(message.text) && botConfig.lastAuthor != null) {
            doNext(ActionResponse.message(slogan.fetchSlogan(botConfig.lastAuthor!!)))
        }
    }

    override fun halp() = "<b>!last</b> uno slogan per l'ultimo autore"

}
