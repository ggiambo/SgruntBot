package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

class ParlaSuper(botUtils: BotUtils) : Action(botUtils) {

    private val parlasuper =
        Regex("^!parlasuper (.*)$", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL))

    override fun doAction(message: Message, context: Context) {
        val testo = parlasuper.findAll(message.text)
            .map { it.groupValues[1] }
            .firstOrNull()
        if (testo != null && setOf<Long>(
                32657811,
                353708759,
                252800958,
                250965179,
                68714652,
                259607683,
                104278889
            ).contains(message.from.id)
        ) {
            botUtils.rispondi(SendMessage("-1001103213994", testo))
            context.lastSuper = message
        }
    }
}