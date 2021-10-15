package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

class ParlaSuper : Action {

    private val parlasuper = Regex(
        "^!parlasuper (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message, context: Context) {
        val testo = parlasuper.find(message.text)?.groupValues?.get(1)
        if (testo != null && BotUtils.instance.userIds.contains(message.from.id)) {
            BotUtils.instance.rispondi(SendMessage(BotUtils.instance.chatId, testo))
            context.lastSuper = message
        }
    }
}