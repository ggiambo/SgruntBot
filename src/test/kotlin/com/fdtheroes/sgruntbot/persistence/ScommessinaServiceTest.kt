package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Scommessina
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock
import java.time.LocalDate
import java.time.LocalDateTime

class ScommessinaServiceTest {

    private val scommessinaRepository = mock<ScommessinaRepository> {
        on { findAllByCreatedBefore(isA()) } doAnswer { args ->
            val date = args.component1<LocalDate>()
            getScommessine().filter { it.created.isBefore(date) }
        }
        on { findAllByCreatedBetween(isA(), isA()) } doAnswer { args ->
            val dateFrom = args.component1<LocalDate>()
            val dateTo = args.component2<LocalDate>()
            getScommessine().filter { it.created.isBefore(dateTo) && it.created.isAfter(dateFrom) }
        }
    }

    private val scommessinaService = ScommessinaService(scommessinaRepository)

    @Test
    fun testWillExpireInThreeDays() {
        val res = scommessinaService.getWillExpireInThreeDays()

        val now = LocalDate.now()
        assertThat(res).hasSize(2)
        assertThat(res.map { it.id }).containsExactlyInAnyOrder(12L, 13L)
        assertThat(res.map { it.created }).containsExactlyInAnyOrder(
            now.minusDays(12),
            now.minusDays(13),
        )
    }

    @Test
    fun testExpired() {
        val res = scommessinaService.getExpired()

        val now = LocalDate.now()
        assertThat(res).hasSize(6)
        assertThat(res.map { it.id }).containsExactlyInAnyOrder(15L, 16L, 17L, 18L, 19L, 20L)
        assertThat(res.map { it.created }).containsExactlyInAnyOrder(
            now.minusDays(15),
            now.minusDays(16),
            now.minusDays(17),
            now.minusDays(18),
            now.minusDays(19),
            now.minusDays(20),
        )
    }

    private fun getScommessine(): List<Scommessina> {
        val now = LocalDate.now()
        return (0..20).map {
            Scommessina(
                id = it.toLong(),
                userId = 42,
                content = "scommessina_${it}",
                created = now.minusDays(it.toLong()),
                messageId = 1000 + it
            )

        }
    }

}