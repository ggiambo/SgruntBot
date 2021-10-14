package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

class DioPorco : Action {

    private val regex = Regex("\\bdio (porco|cane)\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun doAction(message: Message, context: Context) {
        if (context.pignolo && regex.containsMatchIn(message.text)) {
            BotUtils.instance.rispondi(message, "Che mi tocca sentire!")
        }
    }

}