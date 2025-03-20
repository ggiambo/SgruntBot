package com.fdtheroes.sgruntbot.handlers.message.oroscopo;

import org.springframework.stereotype.Service;
import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.time.LocalDate

@Service
class PlanetUtils(private val moonUtils: MoonUtils) {

    fun getPlanetPosition(planet: Planet, currentDate: LocalDate): PlanetPosition {
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
