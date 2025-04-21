package com.fdtheroes.sgruntbot

import java.time.LocalDate
import java.time.Month
import java.time.Month.*

enum class Users(val id: Long, val compleanno: Compleanno? = null) {
    SPDT(24492275),
    DANIELE(32657811, Compleanno(13, FEBRUARY)),
    SHDX_T(68714652, Compleanno(11, JULY)),
    IL_VINCI(104278889, Compleanno(24, AUGUST)),
    ALLEGRA_SOLITUDINE(250965179, Compleanno(12, AUGUST)),
    DA_DA_212(252800958, Compleanno(6, AUGUST)),
    F(259607683, Compleanno(5, APRIL)),
    GGIAMBO(353708759, Compleanno(9, APRIL)),
    BLAH_BANF_BOT(2097709389),
    ALE(5770928065, Compleanno(25, NOVEMBER)),
    GENGIVA_BOT(6383648928),
    DADUNKEN_BOT(6470939064)
    ;

    companion object {
        private val userIds = entries.associateBy { it.id }
        fun byId(id: Long) = userIds[id]
        val compleanni = entries
            .filter { it.compleanno != null }
            .sortedBy { it.compleanno }
    }
}

class Compleanno(private val giorno: Int, private val mese: Month) : Comparable<Compleanno> {
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
