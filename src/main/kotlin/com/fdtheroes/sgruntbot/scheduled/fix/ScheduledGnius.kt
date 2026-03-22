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
        val newsSera = LocalDateTime.now().hour == 20
        val gnius = if (newsSera) {
            redditGnius.getGnius("anime_titties", "worldnews")
        } else {
            redditGnius.getGnius()
        }
        if (gnius.isNotEmpty()) {
            botUtils.messaggio(ActionResponse.message(gnius.joinToString(separator = "\n")))
        }
    }

    override fun firstRun(): LocalDateTime {
        val now = LocalDateTime.now()

        val runMattina = oggiAlle(8)
        if (now < runMattina) {
            return runMattina
        }

        val runSera = oggiAlle(20)
        if (now < runSera) {
            return runSera
        }

        return runMattina.plusDays(1)  // Domani mattina alle 8
    }

    override fun nextRun() = firstRun()

}
