package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class ChiEra : Action, HasHalp {

    private val regex = Regex(
        "^!chiera$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text) && Context.lastSuper != null) {
            BotUtils.rispondi(message, BotUtils.getUserLink(Context.lastSuper))
        }
    }

    override fun halp() = "<b>!chiera</b> ti dice chi ha usato per ultimo il comando !super"
}