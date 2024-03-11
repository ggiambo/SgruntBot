package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.Karma
import com.fdtheroes.sgruntbot.persistence.KarmaRepository
import com.fdtheroes.sgruntbot.persistence.KarmaService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.LocalDate
import java.util.*

class KarmaServiceTest : BaseTest() {

    private val karma = Karma(
        karma = 42,
        karmaCredit = 3,
        creditUpdated = LocalDate.now(),
        userId = 99,
    )

    private val oldKarma = Karma(
        karma = 66,
        karmaCredit = 1,
        creditUpdated = LocalDate.now().minusDays(1),
        userId = 199,
    )

    private val karmaRepository = mock<KarmaRepository> {
        on { getByUserId(eq(99)) } doReturn karma
        on { findById(eq(99)) } doReturn Optional.of(karma)
        on { getByUserId(eq(199)) } doReturn oldKarma
        on { findById(eq(199)) } doReturn Optional.of(oldKarma)
        onGeneric { save(isA()) } doAnswer { it.arguments.firstOrNull() as Karma }
    }

    private val karmaService: KarmaService = KarmaService(botUtils, karmaRepository)

    @Test
    fun getKarma() {
        val karma = karmaService.getKarma(99)

        val getByUserIdCaptor = argumentCaptor<Long>()
        verify(karmaRepository, times(1)).getByUserId(getByUserIdCaptor.capture())
        Assertions.assertThat(getByUserIdCaptor.firstValue).isEqualTo(99)
        Assertions.assertThat(karma.userId).isEqualTo(99)
    }

    @Test
    fun updateCredit_inc() {
        karmaService.updateCredit(99, Int::inc)

        val getByUserIdCaptor = argumentCaptor<Long>()
        verify(karmaRepository, times(1)).getByUserId(getByUserIdCaptor.capture())
        Assertions.assertThat(getByUserIdCaptor.firstValue).isEqualTo(99)
        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(1)).save(saveCaptor.capture())
        Assertions.assertThat(saveCaptor.firstValue.userId).isEqualTo(99)
        Assertions.assertThat(saveCaptor.firstValue.karmaCredit).isEqualTo(4)
    }

    @Test
    fun updateCredit_dec() {
        karmaService.updateCredit(99, Int::dec)

        val getByUserIdCaptor = argumentCaptor<Long>()
        verify(karmaRepository, times(1)).getByUserId(getByUserIdCaptor.capture())
        Assertions.assertThat(getByUserIdCaptor.firstValue).isEqualTo(99)
        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(1)).save(saveCaptor.capture())
        Assertions.assertThat(saveCaptor.firstValue.userId).isEqualTo(99)
        Assertions.assertThat(saveCaptor.firstValue.karmaCredit).isEqualTo(2)
    }

    @Test
    fun precheck_existing() {
        karmaService.precheck(99)

        verify(karmaRepository, times(1)).findById(any())
        verify(karmaRepository, times(0)).save(any())
    }

    @Test
    fun precheck_missing() {
        karmaService.precheck(-1)

        verify(karmaRepository, times(1)).findById(any())

        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(1)).save(saveCaptor.capture())
        Assertions.assertThat(saveCaptor.firstValue.karma).isEqualTo(0)
        Assertions.assertThat(saveCaptor.firstValue.karmaCredit).isEqualTo(5)
        Assertions.assertThat(saveCaptor.firstValue.creditUpdated).isEqualTo(LocalDate.now())
        Assertions.assertThat(saveCaptor.firstValue.userId).isEqualTo(-1)
    }

    @Test
    fun precheck_old() {
        karmaService.precheck(199)

        verify(karmaRepository, times(1)).findById(any())

        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(1)).save(saveCaptor.capture())
        Assertions.assertThat(saveCaptor.firstValue.karma).isEqualTo(66)
        Assertions.assertThat(saveCaptor.firstValue.karmaCredit).isEqualTo(5)   // credit updated
        Assertions.assertThat(saveCaptor.firstValue.creditUpdated).isEqualTo(LocalDate.now()) // date updated
        Assertions.assertThat(saveCaptor.firstValue.userId).isEqualTo(199)
    }

    @Test
    fun takeGiveKarma_inc() {
        karmaService.takeGiveKarma(99, 199, Int::inc)

        verify(karmaRepository, times(1)).getByUserId(eq(199))  // update karma
        verify(karmaRepository, times(1)).getByUserId(eq(99)) // update credit

        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(2)).save(saveCaptor.capture()) // update karma,credit

        val updateKarma = saveCaptor.firstValue
        Assertions.assertThat(updateKarma.karma).isEqualTo(67)
        Assertions.assertThat(updateKarma.karmaCredit).isEqualTo(1)
        Assertions.assertThat(updateKarma.creditUpdated).isEqualTo(LocalDate.now().minusDays(1))
        Assertions.assertThat(updateKarma.userId).isEqualTo(199)

        val updateCredit = saveCaptor.secondValue
        Assertions.assertThat(updateCredit.karma).isEqualTo(42)
        Assertions.assertThat(updateCredit.karmaCredit).isEqualTo(2)
        Assertions.assertThat(updateCredit.creditUpdated).isEqualTo(LocalDate.now())
        Assertions.assertThat(updateCredit.userId).isEqualTo(99)
    }

    @Test
    fun takeGiveKarma_dec() {
        karmaService.takeGiveKarma(99, 199, Int::dec)

        verify(karmaRepository, times(1)).getByUserId(eq(199))  // update karma
        verify(karmaRepository, times(1)).getByUserId(eq(99)) // update credit

        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(2)).save(saveCaptor.capture()) // update karma,credit

        val updateKarma = saveCaptor.firstValue
        Assertions.assertThat(updateKarma.karma).isEqualTo(65)
        Assertions.assertThat(updateKarma.karmaCredit).isEqualTo(1)
        Assertions.assertThat(updateKarma.creditUpdated).isEqualTo(LocalDate.now().minusDays(1))
        Assertions.assertThat(updateKarma.userId).isEqualTo(199)

        val updateCredit = saveCaptor.secondValue
        Assertions.assertThat(updateCredit.karma).isEqualTo(42)
        Assertions.assertThat(updateCredit.karmaCredit).isEqualTo(2)
        Assertions.assertThat(updateCredit.creditUpdated).isEqualTo(LocalDate.now())
        Assertions.assertThat(updateCredit.userId).isEqualTo(99)
    }

    @Test
    fun testTakeGiveKarmaRoulette_inc() {
        karmaService.takeGiveKarma(99, Int::inc)

        verify(karmaRepository, times(1)).getByUserId(eq(99)) // update credit

        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(1)).save(saveCaptor.capture()) // update karma,credit

        val updateKarma = saveCaptor.firstValue
        Assertions.assertThat(updateKarma.karma).isEqualTo(43)
        Assertions.assertThat(updateKarma.karmaCredit).isEqualTo(3)
        Assertions.assertThat(updateKarma.creditUpdated).isEqualTo(LocalDate.now())
        Assertions.assertThat(updateKarma.userId).isEqualTo(99)
    }

    @Test
    fun testTakeGiveKarmaRoulette_dec() {
        karmaService.takeGiveKarma(99, Int::dec)

        verify(karmaRepository, times(1)).getByUserId(eq(99)) // update credit

        val saveCaptor = argumentCaptor<Karma>()
        verify(karmaRepository, times(1)).save(saveCaptor.capture()) // update karma,credit

        val updateKarma = saveCaptor.firstValue
        Assertions.assertThat(updateKarma.karma).isEqualTo(41)
        Assertions.assertThat(updateKarma.karmaCredit).isEqualTo(3)
        Assertions.assertThat(updateKarma.creditUpdated).isEqualTo(LocalDate.now())
        Assertions.assertThat(updateKarma.userId).isEqualTo(99)
    }

}
