package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.actions.models.ErrePiGi
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class ErrePiGiServiceTest : BaseTest() {

    private val errePiGiRepository: ErrePiGiRepository = mock {
        on { findAll() } doReturn status()
        on { getErrePiGiByUserId(isA()) } doAnswer {
            val id = it.arguments.first() as Long
            status().first { it.userId == id }
        }
    }
    private val errePiGiService = ErrePiGiService(botUtils, errePiGiRepository)

    @Test
    @DisplayName("Attaccante morto")
    fun testAttacca_attaccanteMorto() {
        val userWithId1 = user(id = 1, userName = "Username_1")
        val userWithId2 = user(id = 2, userName = "Username_2")

        val attacca = errePiGiService.attacca(userWithId1, userWithId2)
            .split("\n")

        assertThat(attacca).isNotEmpty()
        assertThat(attacca).hasSize(2)
        assertThat(attacca[0]).isEqualTo("Sei morto, non puoi attaccare.")
        assertThat(attacca[1]).isEqualTo("Aspetta fino a domani per riprovare.")

        verify(errePiGiRepository, times(0)).createErrePiGi(isA())
        verify(errePiGiRepository, times(1)).getErrePiGiByUserId(isA())
        verify(errePiGiRepository, times(0)).save(isA())
    }

    @Test
    @DisplayName("Difensore morto")
    fun testAttacca_difensoreMorto() {
        val userWithId1 = user(id = 1, userName = "Username_1")
        val userWithId2 = user(id = 2, userName = "Username_2")
        val attacca = errePiGiService.attacca(userWithId2, userWithId1)
            .split("\n")

        assertThat(attacca).isNotEmpty()
        assertThat(attacca).hasSize(2)
        assertThat(attacca[0]).isEqualTo("Vile, vuoi attaccare <a href=\"tg://user?id=1\">Username_1</a> che è già morto!")
        assertThat(attacca[1]).isEqualTo("Aspetta fino a domani per riprovare.")

        verify(errePiGiRepository, times(0)).createErrePiGi(isA())
        verify(errePiGiRepository, times(2)).getErrePiGiByUserId(isA())
        verify(errePiGiRepository, times(0)).save(isA())
    }

    @Test
    @DisplayName("Già attaccato")
    fun testAttacca_giaAttaccato() {
        val userWithId2 = user(id = 2, userName = "Username_2")
        val userWithId3 = user(id = 3, userName = "Username_3")
        val attacca = errePiGiService.attacca(userWithId2, userWithId3)
            .split("\n")

        assertThat(attacca).isNotEmpty()
        assertThat(attacca).hasSize(2)
        assertThat(attacca[0]).isEqualTo("Oggi hai già attaccato <a href=\"tg://user?id=3\">Username_3</a>.")
        assertThat(attacca[1]).isEqualTo("Aspetta fino a domani per riprovare.")

        verify(errePiGiRepository, times(0)).createErrePiGi(isA())
        verify(errePiGiRepository, times(2)).getErrePiGiByUserId(isA())
        verify(errePiGiRepository, times(0)).save(isA())
    }

    @Test
    fun testAttacca() {
        val userWithId3 = user(id = 3, userName = "Username_3")
        val userWithId4 = user(id = 2, userName = "Username_4")
        val attacca = errePiGiService.attacca(userWithId3, userWithId4)
            .split("\n")

        assertThat(attacca).isNotEmpty()
        assertThat(attacca).hasSize(4)
        assertThat(attacca[0]).startsWith("<b>Username_3 attacca <a href=\"tg://user?id=2\">Username_4</a> con ")
        assertThat(attacca[1]).isEmpty()
        assertThat(attacca[2]).startsWith("Username_3 ha ").endsWith(" punti-vita.")
        assertThat(attacca[3]).startsWith("<a href=\"tg://user?id=2\">Username_4</a> ha ").endsWith(" punti-vita.")

        verify(errePiGiRepository, times(0)).createErrePiGi(isA())
        verify(errePiGiRepository, times(2)).getErrePiGiByUserId(isA())
        verify(errePiGiRepository, times(2)).save(isA())
    }

    @Test
    fun testTestoErrePiGiReport() {
        val testoErrePiGiReport = errePiGiService.testoErrePiGiReport(sgruntBot::getChatMember)
            .orEmpty()
            .split("\n")

        assertThat(testoErrePiGiReport).isNotEmpty()
        assertThat(testoErrePiGiReport).hasSize(4)
        assertThat(testoErrePiGiReport[0]).isEqualTo("Username_1 è moruto. È stato attaccato da Username_1, Username_2.")
        assertThat(testoErrePiGiReport[1]).isEqualTo("Username_2 ha 5 punti-vita. È stato attaccato da nessuno.")
        assertThat(testoErrePiGiReport[2]).isEqualTo("Username_3 ha 4 punti-vita. È stato attaccato da Username_4, Username_2.")
        assertThat(testoErrePiGiReport[3]).isEqualTo("Username_4 ha 2 punti-vita. È stato attaccato da Username_1, Username_4.")
    }

    private fun status(): List<ErrePiGi> {
        return listOf(
            ErrePiGi(
                userId = 1,
                hp = 0,
                attaccantiIds = "1,2"
            ),
            ErrePiGi(
                userId = 2,
                hp = 6,
                attaccantiIds = ""
            ),
            ErrePiGi(
                userId = 3,
                hp = 6,
                attaccantiIds = "4,2"
            ),
            ErrePiGi(
                userId = 4,
                hp = 6,
                attaccantiIds = "1,4"
            )
        )
    }
}