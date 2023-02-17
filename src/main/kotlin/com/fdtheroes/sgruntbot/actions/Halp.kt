package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Halp(
    sgruntBot: SgruntBot,
    private val halp: List<HasHalp>,
) : Action(sgruntBot) {

    private val regex = Regex("!help", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val risposta = halp
                .sortedBy { it.javaClass.simpleName }
                .joinToString("\n") { it.halp() }
            sgruntBot.rispondi(message, risposta)
        }
    }
}