package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.Users.*
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.UsersService
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.Month.*

@Service
class Compleanni(private val botUtils: BotUtils, private val usersService: UsersService) : Scheduled {

    private val compleanniUtenti = listOf(
        CompleannoUtente(Compleanno(13, FEBRUARY), DANIELE),
        CompleannoUtente(Compleanno(5, APRIL), F),
        CompleannoUtente(Compleanno(9, APRIL), GGIAMBO),
        CompleannoUtente(Compleanno(11, JULY), SHDX_T),
        CompleannoUtente(Compleanno(6, AUGUST), DA_DA_212),
        CompleannoUtente(Compleanno(12, AUGUST), MALUCAPERDIDA),
        CompleannoUtente(Compleanno(24, AUGUST), IL_VINCI),
        CompleannoUtente(Compleanno(25, NOVEMBER), ALE),
    ).sortedBy { it.compleanno }

    override fun firstRun() = getProssimoCompleanno()

    override fun nextRun() = getProssimoCompleanno()

    override fun execute() {
        val prossimoCompleannoUtente = getProssimoCompleannoUtente()
        val utenteId = prossimoCompleannoUtente.utente.id
        val user = botUtils.getChatMember(utenteId)
        val userLink = botUtils.getUserLink(user)
        botUtils.messaggio(ActionResponse.message("Tanti auguri $userLink !"))
    }

    private fun getProssimoCompleannoUtente(): CompleannoUtente {
        val now = LocalDate.now()
        val prossimoCompleannoUtente = compleanniUtenti.firstOrNull {
            it.compleanno.data().isAfter(now)
        }
        if (prossimoCompleannoUtente != null) {
            return prossimoCompleannoUtente
        }

        return compleanniUtenti.first()
    }

    private fun getProssimoCompleanno(): LocalDateTime {
        val prossimoCompleanno = getProssimoCompleannoUtente().compleanno.data().atStartOfDay()
        if (prossimoCompleanno.isAfter(LocalDateTime.now())) {
            return prossimoCompleanno
        }

        return prossimoCompleanno.plusYears(1)
    }

    class CompleannoUtente(val compleanno: Compleanno, val utente: Users)

    class Compleanno(val giorno: Int, val mese: Month) : Comparable<Compleanno> {
        override fun compareTo(other: Compleanno): Int {
            val meseCompare = mese.compareTo(other.mese)
            if (meseCompare != 0) {
                return meseCompare
            }
            return giorno.compareTo(other.giorno)
        }

        fun data(): LocalDate {
            val now = LocalDate.now()
            return LocalDate.of(now.year, mese, giorno)
        }

    }

}