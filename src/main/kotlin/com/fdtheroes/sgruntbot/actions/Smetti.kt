package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime

@Service
class Smetti(private val botConfig: BotConfig) : Action {

    private val regex = Regex("(^@?(sgrunty?)|(BlahBanf)(bot)?) .*smetti.*", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            botConfig.pausedTime = LocalDateTime.now().plusMinutes(5)
            sgruntBot.rispondi(message, "Ok, sto zitto 5 minuti. :(")
        }
    }
}
