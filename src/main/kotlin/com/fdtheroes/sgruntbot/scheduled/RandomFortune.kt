package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.Fortune
import org.springframework.stereotype.Service

@Service
class RandomFortune(
    private val fortune: Fortune,
    sgruntBot: SgruntBot,
    botUtils: BotUtils,
) : RandomScheduledAction(sgruntBot, botUtils) {

    override fun getMessageText() = fortune.getFortune()

}