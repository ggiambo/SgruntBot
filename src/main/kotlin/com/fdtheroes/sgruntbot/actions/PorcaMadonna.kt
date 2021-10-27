package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class PorcaMadonna : Action {

    private val regex = Regex("\\bporca madonna\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(message: Message, context: Context) {
        if (context.pignolo && regex.containsMatchIn(message.text)) {
            BotUtils.instance.rispondi(message, "...e tutti gli angeli in colonna!")
        }
    }

}