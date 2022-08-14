package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Stats(
    private val statsService: StatsService,
    private val botUtils: BotUtils,
) : Action {

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (!botUtils.isMessageInChat(message)) {
            return
        }

        if (message.from.isBot) {
            return
        }

        statsService.increaseStats(message.from.id)
    }
}