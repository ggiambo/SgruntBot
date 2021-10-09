package com.fdtheroes.sgruntbot.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OreUtils {

    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    private val ore = listOf(
        "mezzanotte",
        "l'una",
        "le due",
        "le tre",
        "le quattro",
        "le cinque",
        "le sei",
        "le sette",
        "le otto",
        "le nove",
        "le dieci",
        "le undici",
        "mezzogiorno"
    )

    private val minuti = listOf(
        "in punto",
        "e cinque",
        "e dieci",
        "e un quarto",
        "e venti",
        "e venticinque",
        "e mezzo",
        "e trentacinque circa",
        "meno venti",
        "meno un quarto",
        "meno dieci",
        "meno cinque"
    )

    fun oreInLettere(t: LocalDateTime): String {
        var h = t.hour
        val m = t.minute / 5
        if (m >= 8) {
            h += 1
        }
        if (h > 12) {
            h -= 12
        }

        return ore[h] + " " + minuti[m] + " (precisamente ${formatter.format(t)}) ok?)"
    }

}