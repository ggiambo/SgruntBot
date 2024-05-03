package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.handlers.message.RedditGnius
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduledGnius(private val botUtils: BotUtils, private val redditGnius: RedditGnius) : Scheduled {

    override fun execute() {
        val gnius = redditGnius.getGnius()
        if (gnius.isNotEmpty()) {
            botUtils.messaggio(ActionResponse.message(gnius.joinToString(separator = "\n")))
        }
    }

    override fun firstRun(): LocalDateTime {
        val hour = LocalDateTime.now().hour
        if (hour >= 16) {
            return oggiAlle(8).plusDays(1)  // Domani mattina
        }
        return oggiAlle(16)
    }

    override fun nextRun() = firstRun()

    private fun oggiAlle(ore: Int): LocalDateTime {
        return LocalDateTime.now()
            .withHour(ore)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)
    }
}
