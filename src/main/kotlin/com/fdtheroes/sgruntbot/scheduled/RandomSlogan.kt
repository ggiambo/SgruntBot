package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.Slogan
import org.springframework.stereotype.Service

@Service
class RandomSlogan(
    private val slogan: Slogan,
    sgruntBot: SgruntBot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    override fun getMessageText(): String {
        val lastAuthor = Context.lastAuthor
        if (lastAuthor == null) {
            return ""
        }

        return slogan.fetchSlogan(lastAuthor)
    }

}