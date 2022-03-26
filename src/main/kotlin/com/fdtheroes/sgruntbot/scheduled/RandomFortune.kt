package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.Fortune
import org.springframework.stereotype.Service

@Service
class RandomFortune(
    private val fortune: Fortune,
    sgruntBot: SgruntBot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    override fun getMessageText() = fortune.getFortune()

}