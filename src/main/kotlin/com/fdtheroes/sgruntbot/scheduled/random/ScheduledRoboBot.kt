package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils

/**
 * [Why doesn't my bot see messages from other bots?](https://core.telegram.org/bots/faq#why-doesn-39t-my-bot-see-messages-from-other-bots)
 *
 * Bots talking to each other could potentially get stuck in unwelcome loops. To avoid this, we decided that bots will
 * not be able to see messages from other bots regardless of mode.
 */
//@Service
class ScheduledRoboBot(private val botUtils: BotUtils) : ScheduledRandom {

    private val risposte = listOf(
        "RoboBot, tu puzzi.",
        "RobBot, in questa ciat non c'è spazio per due Bot.",
        "RobBot, il mio fratellino ritardato!",
        "RobBot tieni, un biglietto di sola andata per il rottamatore!",
        "RobBot: \"BEEP-BOOP! SONO UN BOT INTELLIGENTE, YUK YUK!\"",
        "RobBot: Prototipo della stupidità artificiale"
    )

    override fun execute() {
        val text = risposte.random()
        botUtils.messaggio(ActionResponse.message(text))
    }

}
