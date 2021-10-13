package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

class ParlaSuper : Action {

    private val regex = Regex(
        "^!parlasuper (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message, context: Context) {
        val testo = regex.findAll(message.text)
            .map { it.groupValues[1] }
            .firstOrNull()
        if (testo != null && BotUtils.instance.userIds.contains(message.from.id)) {
            BotUtils.instance.rispondi(SendMessage(BotUtils.instance.chatId, testo))
            context.lastSuper = message
        }
    }
}