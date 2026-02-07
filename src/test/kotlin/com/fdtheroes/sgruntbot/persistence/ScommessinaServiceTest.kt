package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Scommessina
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock
import java.time.LocalDate

class ScommessinaServiceTest {

    private val scommessinaRepository = mock<ScommessinaRepository> {
        on { findAllByCreatedBeforeAndParticipantsUserIdIsEmpty(isA()) } doAnswer { args ->
            val date = args.component1<LocalDate>()
            getScommessine().filter {
                it.participantsUserId.isEmpty() && it.created.isBefore(date)
            }
        }
        on { findAllByCreatedBetweenAndParticipantsUserIdIsEmpty(isA(), isA()) } doAnswer { args ->
            val dateFrom = args.component1<LocalDate>()
            val dateTo = args.component2<LocalDate>()
            getScommessine().filter {
                it.participantsUserId.isEmpty() && it.created.isBefore(dateTo) && it.created.isAfter(dateFrom)
            }
        }
    }

    private val scommessinaService = ScommessinaService(scommessinaRepository)

    @Test
    fun testWillExpireInThreeDays() {
        val res = scommessinaService.getNoParticipantsAndWillExpireInThreeDays()

        val now = LocalDate.now()
        assertThat(res).hasSize(1)
        assertThat(res.map { it.id }).containsExactlyInAnyOrder(13L)
        assertThat(res.map { it.created }).containsExactlyInAnyOrder(
            now.minusDays(13),
        )
    }

    @Test
    fun testExpired() {
        val res = scommessinaService.getNoParticipantsAndExpired()

        val now = LocalDate.now()
        assertThat(res).hasSize(3)
        assertThat(res.map { it.id }).containsExactlyInAnyOrder(15L, 17L, 19L)
        assertThat(res.map { it.created }).containsExactlyInAnyOrder(
            now.minusDays(15),
            now.minusDays(17),
            now.minusDays(19),
        )
    }

    private fun getScommessine(): List<Scommessina> {
        val now = LocalDate.now()
        return (0..20L).map {
            Scommessina(
                id = it,
                userId = 42,
                content = "scommessina_${it}",
                created = now.minusDays(it),
                messageId = 1000 + it.toInt(),
                participantsUserId = if (it.mod(2) == 0) listOf(it + 1000) else emptyList()
            )
        }
    }

}