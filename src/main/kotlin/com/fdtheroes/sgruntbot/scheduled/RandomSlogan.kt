package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.actions.Slogan
import org.springframework.stereotype.Service

@Service
class RandomSlogan(
    private val slogan: Slogan,
    sgruntBot: Bot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    override fun getMessageText(): String {
        val lastAuthor = botConfig.lastAuthor
        if (lastAuthor == null) {
            return ""
        }

        return slogan.fetchSlogan(lastAuthor)
    }

}
