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
        if (hour >= 18) {
            return oggiAlle(0).plusDays(1)  // mezzanotte è l'inizio di domani
        }
        if (hour >= 12) {
            return oggiAlle(18)
        }
        if (hour >= 6) {
            return oggiAlle(12)
        }
        return oggiAlle(6)
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
