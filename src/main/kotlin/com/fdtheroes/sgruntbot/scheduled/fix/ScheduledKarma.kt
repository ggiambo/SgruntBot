package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScheduledKarma(private val karmaService: KarmaService, private val sgruntBot: Bot) : ScheduledAMezzanotte {

    override fun firstRun(): LocalDateTime {
        val mezzanotte = LocalDateTime.now()
            .withHour(0)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        return mezzanotte.plusDays(1)
    }

    override fun execute() {
        val testo = karmaService.testoKarmaReport(sgruntBot)
        sgruntBot.messaggio(ActionResponse.message(testo, false))
    }
}
