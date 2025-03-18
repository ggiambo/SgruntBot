package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import swisseph.SweConst
import swisseph.SweDate
import swisseph.SwissEph
import java.time.LocalDate

data class HoroscopeParams(
    val sign: Sign,
    val planets: List<PlanetPosition>,
    val conjunctions: List<List<PlanetPosition>>,
    val oppositions: List<List<PlanetPosition>>,
) {
    fun toStringInItalian(): String {
        val sb = StringBuilder()

        planets.forEach { planet ->
            sb.append("- ${planet.planet.nome} ")
            if (planet.planet == Planet.LUNA) {
                sb.append("${planet.moonPhase} ")
            }
            when {
                planet.enteringSign == sign -> sb.append(
                    "sta entrando ${
                        planet.enteringSign.getSignNameWithPreposition(
                            "in"
                        )
                    }"
                )

                planet.leavingSign == sign -> sb.append(
                    "sta uscendo da ${
                        planet.leavingSign.getSignNameWithPreposition("da")
                    }"
                )

                else -> sb.append("è ${planet.sign.getSignNameWithPreposition("in")}")
            }
            if (planet.retrograde) {
                sb.append(" in stazione retrograda")
            }
            sb.append("\n")
        }

        conjunctions.forEach { conjunction ->
            val planetA = conjunction[0]
            val planetB = conjunction[1]
            sb.append("- ${planetA.planet.nome} è in congiunzione con ${planetB.planet.nome}\n")
        }

        oppositions.forEach { opposition ->
            val planetA = opposition[0]
            val planetB = opposition[1]
            sb.append("- ${planetA.planet.nome} è in opposizione con ${planetB.planet.nome}\n")
        }

        if (planets.isEmpty()) {
            sb.append("- Non sono presenti pianeti nel segno")
        }

        return sb.toString()
    }
}

class PlanetPosition(
    val longitude: Double,
    val sign: Sign,
    val longitudeIntoSign: Double,
    val planet: Planet,
    val retrograde: Boolean = false,
    val enteringSign: Sign? = null,
    val leavingSign: Sign? = null,
    val moonPhase: String? = null, // New field for moon phase
) {
    fun hasSign(sign: Sign): Boolean {
        return this.sign == sign || enteringSign == sign || leavingSign == sign
    }
}

private fun getPlanetPositionInstant(planet: Planet, hour: Double, currentDate: LocalDate): PlanetPosition {
    val sw = SwissEph()
    val julianDay = SweDate.getJulDay(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, hour)
    val sunPosition = DoubleArray(6)
    val errorMsg = StringBuffer()
    val returnFlag =
        sw.swe_calc(julianDay, planet.index, SweConst.SEFLG_SWIEPH + SweConst.SEFLG_EQUATORIAL, sunPosition, errorMsg)

    if (returnFlag < 0) {
        throw Exception("Error: ${errorMsg.toString()}")
    }

    val longitude = sunPosition[0]
    val sign = Sign.byIndex((longitude / 30).toInt()) // 30 degrees per zodiac sign
    return PlanetPosition(longitude, sign!!, longitude % 30, planet)
}

private fun getPlanetPosition(planet: Planet, currentDate: LocalDate): PlanetPosition {
    val positionAtStart = getPlanetPositionInstant(planet, 0.0, currentDate)
    val positionAtNoon = getPlanetPositionInstant(planet, 12.0, currentDate)
    val positionAtEnd = getPlanetPositionInstant(planet, 23.9, currentDate)

    val retrograde = positionAtEnd.longitude < positionAtStart.longitude
    val enteringSign = if (positionAtEnd.sign != positionAtStart.sign) positionAtEnd.sign else null
    val leavingSign = if (positionAtEnd.sign != positionAtStart.sign) positionAtStart.sign else null

    val moonPhase = if (planet == Planet.LUNA) {
        getMoonPhaseString(currentDate) // Calculate moon phase if the planet is the moon
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

private fun getPlanetsPosition(currentDate: LocalDate): Array<PlanetPosition> {
    return Planet.entries.map { getPlanetPosition(it, currentDate) }.toTypedArray()
}

fun getHoroscopeParams(sign: Sign, currentDate: LocalDate = LocalDate.now()): HoroscopeParams {
    val allPlanets = getPlanetsPosition(currentDate)
    val conjunctions = mutableListOf<List<PlanetPosition>>()
    val oppositions = mutableListOf<List<PlanetPosition>>()

    for (i in allPlanets.indices) {
        val planetA = allPlanets[i]
        if (!planetA.hasSign(sign)) {
            continue
        }
        for (j in i + 1 until allPlanets.size) {
            val planetB = allPlanets[j]

            if (Math.abs(planetA.longitude - planetB.longitude) <= 1) {
                conjunctions.add(listOf(planetA, planetB))
            }
            val longitudeDifference = Math.abs((planetA.longitude - planetB.longitude + 360) % 360)
            if (Math.abs(longitudeDifference - 180) <= 1) {
                oppositions.add(listOf(planetA, planetB))
            }
        }
    }

    val signPlanets = allPlanets.filter { it.sign == sign }.toList()
    return HoroscopeParams(sign, signPlanets, conjunctions, oppositions) // sign is set to -1 as it's not used
}

private fun getMoonPhase(hour: Double, currentDate: LocalDate): Double {
    val sw = SwissEph()
    val julianDay = SweDate.getJulDay(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, hour)
    val moonPhase = DoubleArray(20)
    val errorMsg = StringBuffer()

    sw.swe_pheno(julianDay, SweConst.SE_MOON, SweConst.SEFLG_MOSEPH, moonPhase, errorMsg)

    if (errorMsg.isNotEmpty()) {
        throw Exception("Error calculating moon phase at hour $hour: ${errorMsg.toString()}")
    }

    return moonPhase[1]
}

private fun getMoonPhaseString(currentDate: LocalDate): String {
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
