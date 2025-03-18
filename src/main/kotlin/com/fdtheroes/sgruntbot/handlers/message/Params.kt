package com.fdtheroes.sgruntbot.handlers.message

import swisseph.*
import java.time.LocalDate

data class HoroscopeParams(
    val sign: Int,
    val planets: List<PlanetPosition>,
    val conjunctions: List<List<PlanetPosition>>,
    val oppositions: List<List<PlanetPosition>>
) {
    fun toStringInItalian(): String {
        val sb = StringBuilder()
        
        planets.forEach { planet ->
            sb.append("- ${planetName(planet.planet)} ")
            if (planet.planet == SweConst.SE_MOON) {
                sb.append("${planet.moonPhase} ")
            }
            when {
                sign != -1 && planet.enteringSign == sign -> sb.append("sta entrando ${getSignNameWithPreposition(planet.enteringSign, "in")}")
                sign != -1 && planet.leavingSign == sign -> sb.append("sta uscendo da ${getSignNameWithPreposition(planet.leavingSign, "da")}")
                else -> sb.append("è ${getSignNameWithPreposition(planet.sign, "in")}")
            }
            if (planet.retrograde) {
                sb.append(" in stazione retrograda")
            }
            sb.append("\n")
        }

        conjunctions.forEach { conjunction ->
            val planetA = conjunction[0]
            val planetB = conjunction[1]
            sb.append("- ${planetName(planetA.planet)} è in congiunzione con ${planetName(planetB.planet)}\n")
        }

        oppositions.forEach { opposition ->
            val planetA = opposition[0]
            val planetB = opposition[1]
            sb.append("- ${planetName(planetA.planet)} è in opposizione con ${planetName(planetB.planet)}\n")
        }
        
        if (planets.size == 0) {
            sb.append("- Non sono presenti pianeti nel segno")
        }

        return sb.toString()
    }
}

class PlanetPosition(
    val longitude: Double,
    val sign: Int,
    val longitudeIntoSign: Double,
    val planet: Int,
    var retrograde: Boolean = false,
    var enteringSign: Int = -1,
    var leavingSign: Int = -1,
    var moonPhase: String? = null // New field for moon phase
) {
    fun hasSign(sign: Int): Boolean {
        return this.sign == sign || enteringSign == sign || leavingSign == sign
    }
}

fun planetName(planet: Int): String {
    return when (planet) {
        SweConst.SE_SUN -> "Il sole"
        SweConst.SE_MOON -> "La luna"
        SweConst.SE_MERCURY -> "Mercurio"
        SweConst.SE_VENUS -> "Venere"
        SweConst.SE_MARS -> "Marte"
        SweConst.SE_JUPITER -> "Giove"
        SweConst.SE_SATURN -> "Saturno"
        SweConst.SE_URANUS -> "Urano"
        SweConst.SE_NEPTUNE -> "Nettuno"
        SweConst.SE_PLUTO -> "Plutone"
        else -> "Sconosciuto"
    }
}

data class SignInfo(val signName: String, val article: String, val inn: String, val da: String)

fun getSignNameWithPreposition(sign: Int, preposition: String): String {
    val tup = when (sign) {
        0 -> SignInfo("Ariete", "l'", "nell'", "dall'")
        1 -> SignInfo("Toro", "il", "nel", "dal")
        2 -> SignInfo("Gemelli", "i", "nei", "dai")
        3 -> SignInfo("Cancro", "il", "nel", "dal")
        4 -> SignInfo("Leone", "il", "nel", "dal")
        5 -> SignInfo("Vergine", "la", "nella", "dalla")
        6 -> SignInfo("Bilancia", "la", "nella", "dalla")
        7 -> SignInfo("Scorpione", "lo", "nello", "dallo")
        8 -> SignInfo("Sagittario", "il", "nel", "dal")
        9 -> SignInfo("Capricorno", "il", "nel", "dal")
        10 -> SignInfo("Acquario", "l'", "nell'", "dall'")
        11 -> SignInfo("Pesci", "i", "nei", "dai")
        else -> SignInfo("Sconosciuto", "", "", "")
    }
    
    val (signName, article, inn, da) = tup
    
    val p = when(preposition) {
        "" -> article
        "in" -> inn
        "da" -> da
        else -> ""
    }
    
    val adjustedP = if (!p.endsWith("'")) "$p " else p
    
    return "$adjustedP$signName"
}

fun getPlanetPositionInstant(planet: Int, hour: Double, currentDate: LocalDate = LocalDate.now()): PlanetPosition {
    val sw = SwissEph()
    val julianDay = SweDate.getJulDay(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, hour)
    val sunPosition = DoubleArray(6)
    val errorMsg = StringBuffer()
    val returnFlag = sw.swe_calc(julianDay, planet, SweConst.SEFLG_SWIEPH + SweConst.SEFLG_EQUATORIAL, sunPosition, errorMsg)

    if (returnFlag < 0) {
        throw Exception("Error: ${errorMsg.toString()}")
    }

    val longitude = sunPosition[0]
    val sign = (longitude / 30).toInt() // 30 degrees per zodiac sign
    return PlanetPosition(longitude, sign, longitude % 30, planet)
}

fun getPlanetPosition(planet: Int): PlanetPosition {
    val positionAtStart = getPlanetPositionInstant(planet, 0.0)
    val positionAtNoon = getPlanetPositionInstant(planet, 12.0)
    val positionAtEnd = getPlanetPositionInstant(planet, 23.9)

    val retrograde = positionAtEnd.longitude < positionAtStart.longitude
    val enteringSign = if (positionAtEnd.sign != positionAtStart.sign) positionAtEnd.sign else -1
    val leavingSign = if (positionAtEnd.sign != positionAtStart.sign) positionAtStart.sign else -1

    val moonPhase = if (planet == SweConst.SE_MOON) {
        getMoonPhaseString() // Calculate moon phase if the planet is the moon
    } else {
        null
    }

    return PlanetPosition(positionAtNoon.longitude, positionAtNoon.sign, positionAtNoon.longitudeIntoSign, planet, retrograde, enteringSign, leavingSign, moonPhase)
}

fun getPlanetsPosition(): Array<PlanetPosition> {
    val planets = listOf(SweConst.SE_SUN, SweConst.SE_MOON, SweConst.SE_MERCURY, SweConst.SE_VENUS, SweConst.SE_MARS,
                         SweConst.SE_JUPITER, SweConst.SE_SATURN, SweConst.SE_URANUS, SweConst.SE_NEPTUNE, SweConst.SE_PLUTO)
    return planets.map { getPlanetPosition(it) }.toTypedArray()
}

fun getHoroscopeParams(sign: Int): HoroscopeParams {
    val allPlanets = getPlanetsPosition()
    val conjunctions = mutableListOf<List<PlanetPosition>>()
    val oppositions = mutableListOf<List<PlanetPosition>>()

    for (i in allPlanets.indices) {
        val planetA = allPlanets[i]
        if ((sign != -1) && !planetA.hasSign(sign)) {
            continue
        }
        for (j in i + 1 until allPlanets.size) {
            val planetB = allPlanets[j]

            if (Math.abs(planetA.longitude - planetB.longitude) <= 1) {
                println("conjunction ${planetName(planetA.planet)} ${planetA.longitude} ${planetName(planetB.planet)} ${planetB.longitude}")
                conjunctions.add(listOf(planetA, planetB))
            }
            val longitudeDifference = Math.abs((planetA.longitude - planetB.longitude + 360) % 360)
            if (Math.abs(longitudeDifference - 180) <= 1) {
                oppositions.add(listOf(planetA, planetB))
            }
        }
    }

    val signPlanets = when (sign) {
        -1 -> allPlanets.toList()
        else -> allPlanets.filter{ it.sign == sign }.toList()
    } 
    return HoroscopeParams(sign, signPlanets, conjunctions, oppositions) // sign is set to -1 as it's not used
}

fun getMoonPhase(hour: Double): Double {
    val sw = SwissEph()
    val currentDate = LocalDate.now()
    val julianDay = SweDate.getJulDay(currentDate.year, currentDate.monthValue, currentDate.dayOfMonth, hour)
    val moonPhase = DoubleArray(20)
    val errorMsg = StringBuffer()

    sw.swe_pheno(julianDay, SweConst.SE_MOON, SweConst.SEFLG_MOSEPH, moonPhase, errorMsg)

    if (errorMsg.isNotEmpty()) {
        throw Exception("Error calculating moon phase at hour $hour: ${errorMsg.toString()}")
    }

    return moonPhase[1]
}

fun getMoonPhaseString(): String {
    val moonPhaseStart = getMoonPhase(0.0)
    val moonPhaseNoon = getMoonPhase(12.0)
    val moonPhaseEnd = getMoonPhase(23.9)

    return when {
        moonPhaseNoon > 0.99 -> "piena"
        moonPhaseNoon < 0.01 -> "nuova"
        moonPhaseStart > moonPhaseEnd -> "calante"
        moonPhaseStart < moonPhaseEnd -> "crescente"
        else -> "sconosciuta"
    }
}
