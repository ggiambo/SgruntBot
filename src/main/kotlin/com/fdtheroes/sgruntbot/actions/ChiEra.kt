package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class ChiEra(
    sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
    private val botConfig: BotConfig
) : Action(sgruntBot), HasHalp {

    private val regex = Regex(
        "^!chiera$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text) && botConfig.lastSuper != null) {
            sgruntBot.rispondi(message, botUtils.getUserLink(botConfig.lastSuper))
        }
    }

    override fun halp() = "<b>!chiera</b> ti dice chi ha usato per ultimo il comando !super"
}
