package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiService
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class ScheduledErrePiGi(
    private val errePiGiService: ErrePiGiService,
    private val sgruntBot: Bot,
) : Timer() {

    val mezzanotte = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }.time

    @PostConstruct
    fun start() {
        val oneDayInMilleseconds = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        Timer().schedule(PublishErrePiGiReport(), mezzanotte, oneDayInMilleseconds)
    }

    inner class PublishErrePiGiReport : TimerTask() {
        override fun run() {
            val testo = errePiGiService.testoErrePiGiReport(sgruntBot::getChatMember)
            if (testo != null) {
                sgruntBot.messaggio(ActionResponse.message(testo))
            }
            errePiGiService.reset()
        }
    }
}
