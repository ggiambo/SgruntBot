package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class PorcoDio(private val botConfig: BotConfig) : Action {

    private val regex = Regex("\\bporco dio\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (botConfig.pignolo && regex.containsMatchIn(message.text)) {
            doNext(ActionResponse.message("E la madooonna!"))
        }
    }

}
