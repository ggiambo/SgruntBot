package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.Slogan
import org.springframework.stereotype.Service

@Service
class RandomSlogan(
    private val slogan: Slogan,
    sgruntBot: SgruntBot,
    botUtils: BotUtils,
) : RandomScheduledAction(sgruntBot, botUtils) {

    override fun getMessageText(): String {
        val lastAuthor = Context.lastAuthor
        if (lastAuthor == null) {
            return ""
        }

        return slogan.fetchSlogan(lastAuthor)
    }

}