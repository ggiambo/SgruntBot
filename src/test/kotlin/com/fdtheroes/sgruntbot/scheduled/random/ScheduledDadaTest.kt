package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ScheduledDadaTest : BaseTest() {

    private val scheduledDada = ScheduledDada(botUtils)

    @Test
    fun firstRun() {
        val firstRun = scheduledDada.firstRun()

        assertThat(firstRun).isAfter(LocalDateTime.now())
        assertThat(firstRun.hour).isBetween(8, 11)
        assertThat(firstRun.minute).isBetween(0, 60)
    }

    @Test
    fun nextRun() {
        val firstRun = scheduledDada.firstRun()

        assertThat(firstRun).isAfter(LocalDateTime.now())
        assertThat(firstRun.dayOfYear).isGreaterThanOrEqualTo(LocalDate.now().dayOfYear) // LOL will fail on 31st December
        assertThat(firstRun.hour).isBetween(8, 11)
        assertThat(firstRun.minute).isBetween(0, 60)
    }

    @Test
    fun execute() {
        scheduledDada.execute()

        assertThat(actionResponses).hasSize(1)
        val response = actionResponses.first()
        assertThat(response.type).isEqualTo(ActionResponseType.Message)
        assertThat(response.message).startsWith("Hola <a href=\"tg://user?id=252800958\">Username_252800958</a> sono le ")
        assertThat(response.message).endsWith(", il momento giusto per ejectare Naghmeh 🚀 !")
    }
}