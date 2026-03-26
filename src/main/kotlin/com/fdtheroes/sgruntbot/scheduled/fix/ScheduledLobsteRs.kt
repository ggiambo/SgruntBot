package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.handlers.message.HackerNews
import com.fdtheroes.sgruntbot.handlers.message.LobsteRs
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduledLobsteRs(private val botUtils: BotUtils, private val lobsteRs: LobsteRs) : Scheduled {

    override fun execute() {
        val messageContent = lobsteRs.getMessageContent()
        botUtils.messaggio(ActionResponse.message(messageContent))
    }

    override fun firstRun(): LocalDateTime {
        val run = oggiAlle(10)
        if (run < LocalDateTime.now()) {
            return run.plusDays(1)
        }
        return run
    }

    override fun nextRun() = firstRun()

}