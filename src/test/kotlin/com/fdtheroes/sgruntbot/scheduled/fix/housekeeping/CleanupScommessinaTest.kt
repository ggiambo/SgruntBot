package com.fdtheroes.sgruntbot.scheduled.fix.housekeeping

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Scommessina
import com.fdtheroes.sgruntbot.persistence.ScommessinaService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CleanupScommessinaTest : BaseTest() {

    private val scommessinaService = mock<ScommessinaService>()
    private val cleanupScommessina = CleanupScommessina(scommessinaService, botUtils)

    @Test
    fun nothingToDo() {
        cleanupScommessina.doCleanup()
        assertThat(actionResponses).isEmpty()
    }

    @Test
    fun deleteExpiredScommessine() {
        whenever { scommessinaService.getNoParticipantsAndExpired() } doReturn
                listOf(
                    scommessina(20),
                )

        cleanupScommessina.doCleanup()

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().message).isEqualTo("Hei <a href=\"tg://user?id=20\">Username_20</a>, la scommesssina <i>Scommessina 20</i> è scaduta e verrà cancellata.")
    }

    @Test
    fun checkWillExpireScommessine() {
        val createdDaysAgo = 5L
        whenever { scommessinaService.getNoParticipantsAndWillExpireInThreeDays() } doReturn
                listOf(
                    scommessina(createdDaysAgo),
                )

        cleanupScommessina.doCleanup()

        val expiration = LocalDate.now().minusDays(createdDaysAgo).plusDays(14).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().message).isEqualTo("Hei <a href=\"tg://user?id=5\">Username_5</a>, la scommesssina <i>Scommessina 5</i> non ha partecipanti e verrà cancellata il $expiration")
    }

    private fun scommessina(createdDaysAgo: Long, participantsUserId: List<Long> = emptyList()) = Scommessina(
        userId = createdDaysAgo,
        content = "Scommessina $createdDaysAgo",
        created = LocalDate.now().minusDays(createdDaysAgo),
        messageId = createdDaysAgo.toInt(),
        participantsUserId = participantsUserId,
        id = createdDaysAgo
    )

}