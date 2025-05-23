package com.fdtheroes.sgruntbot.handlers.message.oroscopo;

import de.thmac.swisseph.SweConst
import de.thmac.swisseph.SweDate
import de.thmac.swisseph.SwissEph
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class PlanetUtils(
    private val moonUtils: MoonUtils,
    private val nowSupplier: () -> LocalDateTime, // used for testing
) {

    fun getPlanetPosition(planet: Planet): PlanetPosition {
        val currentDate = nowSupplier().toLocalDate()
        val positionAtStart = getPlanetPositionInstant(planet, 0.0, currentDate)
        val positionAtNoon = getPlanetPositionInstant(planet, 12.0, currentDate)
        val positionAtEnd = getPlanetPositionInstant(planet, 23.9, currentDate)

        val retrograde = positionAtEnd.longitude < positionAtStart.longitude
        val enteringSign = if (positionAtEnd.sign != positionAtStart.sign) positionAtEnd.sign else null
        val leavingSign = if (positionAtEnd.sign != positionAtStart.sign) positionAtStart.sign else null

        val moonPhase = if (planet == Planet.LUNA) {
            moonUtils.getMoonPhaseString(currentDate) // Calculate moon phase if the planet is the moon
        } else {
            null
        }

        return PlanetPosition(
            positionAtNoon.longitude,
            positionAtNoon.sign,
            positionAtNoon.longitudeIntoSign,
            planet,
            retrograde,
            enteringSign,
            leavingSign,
            moonPhase
        )
    }

    private fun getPlanetPositionInstant(planet: Planet, hour: Double, currentDate: LocalDate): PlanetPosition {
        val julianDay = SweDate.getJulDay(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, hour)
        val sunPosition = DoubleArray(6)
        val errorMsg = StringBuffer()
        val returnFlag = SwissEph().swe_calc(
            julianDay,
            planet.index,
            SweConst.SEFLG_SWIEPH + SweConst.SEFLG_EQUATORIAL,
            sunPosition,
            errorMsg
        )

        if (returnFlag < 0) {
            throw Exception("Error: $errorMsg")
        }

        val longitude = sunPosition[0]
        val sign = Sign.byIndex((longitude / 30).toInt()) // 30 degrees per zodiac sign
        return PlanetPosition(longitude, sign!!, longitude % 30, planet)
    }

}
