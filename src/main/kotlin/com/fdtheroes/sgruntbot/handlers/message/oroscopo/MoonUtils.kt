package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import org.springframework.stereotype.Component
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.time.LocalDate

@Component
class MoonUtils {

    fun getMoonPhaseString(currentDate: LocalDate): String {
        val moonPhaseStart = getMoonPhase(0.0, currentDate)
        val moonPhaseNoon = getMoonPhase(12.0, currentDate)
        val moonPhaseEnd = getMoonPhase(23.9, currentDate)

        return when {
            moonPhaseNoon > 0.99 -> "piena"
            moonPhaseNoon < 0.01 -> "nuova"
            moonPhaseStart > moonPhaseEnd -> "calante"
            moonPhaseStart < moonPhaseEnd -> "crescente"
            else -> "sconosciuta"
        }
    }

    private fun getMoonPhase(hour: Double, currentDate: LocalDate): Double {
        val julianDay = SweDate.getJulDay(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, hour)
        val moonPhase = DoubleArray(20)
        val errorMsg = StringBuffer()

        SwissEph().swe_pheno(julianDay, SweConst.SE_MOON, SweConst.SEFLG_MOSEPH, moonPhase, errorMsg)

        if (errorMsg.isNotEmpty()) {
            throw Exception("Error calculating moon phase at hour $hour: $errorMsg")
        }

        return moonPhase[1]
    }

}