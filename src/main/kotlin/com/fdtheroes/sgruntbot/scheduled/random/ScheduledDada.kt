package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.random.Random.Default.nextInt

@Service
class ScheduledDada(private val botUtils: BotUtils) : Scheduled {

    private val timeFormatter = DateTimeFormatter
        .ofLocalizedTime(FormatStyle.SHORT)
        .withLocale(Locale.ITALIAN)

    override fun firstRun(): LocalDateTime {
        val ret = traLeOttoELeUndici()
        if (ret.isBefore(LocalDateTime.now())) {
            return ret.plusDays(1)
        }
        return ret
    }

    override fun nextRun(): LocalDateTime = traLeOttoELeUndici().plusDays(1)

    override fun execute() {
        val dada = botUtils.getUserLink(botUtils.getChatMember(Users.DA_DA_212.id))
        val now = LocalTime.now().format(timeFormatter)
        val testo = "Hola $dada sono le $now, il momento giusto per ejectare Naghmeh \uD83D\uDE80 !"
        botUtils.messaggio(ActionResponse.message(testo))
    }

    private fun traLeOttoELeUndici(): LocalDateTime {
        return LocalDate.now().atStartOfDay()
            .withHour(nextInt(8, 11))
            .withMinute(nextInt(0, 60))
    }
}