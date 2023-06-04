package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class DioPorco(private val botConfig: BotConfig) : Action {

    private val regex = Regex("\\bdio (porco|cane)\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (botConfig.pignolo && regex.containsMatchIn(message.text)) {
            doNext(ActionResponse.message("Che mi tocca sentire!"))
        }
    }

}
