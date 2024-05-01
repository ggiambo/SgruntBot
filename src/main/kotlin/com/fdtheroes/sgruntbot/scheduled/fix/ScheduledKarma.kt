package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.KarmaService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service

@Service
class ScheduledKarma(private val botUtils: BotUtils, private val karmaService: KarmaService) : ScheduledAMezzanotte {

    override fun execute() {
        val testo = karmaService.testoKarmaReport()
        botUtils.messaggio(ActionResponse.message(testo))
    }
}
