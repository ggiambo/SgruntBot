package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Id : Action, HasHalp {

    private val regex = Regex("^!id$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.matches(message.text)) {
            BotUtils.rispondi(message, "Il tuo id: ${message.from.id}")
        }
    }

    override fun halp() = "<b>!id</b> mostra il tuo id di Telegram"
}
