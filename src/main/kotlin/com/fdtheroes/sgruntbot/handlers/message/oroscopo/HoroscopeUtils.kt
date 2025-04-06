package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class HoroscopeUtils(private val planetUtils: PlanetUtils) {

    fun getHoroscopeParams(sign: Sign): HoroscopeParams {
        val allPlanets = Planet.entries.map { planetUtils.getPlanetPosition(it) }
        val conjunctions = mutableListOf<List<PlanetPosition>>()
        val oppositions = mutableListOf<List<PlanetPosition>>()

        allPlanets.forEachIndexed { idxPlanetA, planetA ->
            if (planetA.hasSign(sign)) {
                allPlanets.subList(idxPlanetA + 1, allPlanets.size).forEach { planetB ->
                    val distance = abs(planetA.longitude - planetB.longitude)
                    if (distance <= 1) {
                        conjunctions.add(listOf(planetA, planetB))
                    }
                    if (distance in 179.0..181.0) {
                        oppositions.add(listOf(planetA, planetB))
                    }
                }
            }
        }

        val signPlanets = allPlanets.filter { it.sign == sign }
        return HoroscopeParams(sign, signPlanets, conjunctions, oppositions) // sign is set to -1 as it's not used
    }

    fun toStringInItalian(horoscopeParams: HoroscopeParams): String {
        val sb = StringBuilder()

        horoscopeParams.planets.forEach { planet ->
            sb.append("- ${planet.planet.nome} ")
            if (planet.planet == Planet.LUNA) {
                sb.append("${planet.moonPhase} ")
            }
            when {
                planet.enteringSign == horoscopeParams.sign -> sb.append(
                    "sta entrando ${planet.enteringSign.entering()}"
                )

                planet.leavingSign == horoscopeParams.sign -> sb.append(
                    "sta uscendo da ${planet.leavingSign.leaving()}"
                )

                else -> sb.append("è ${planet.sign.isIn()}")
            }
            if (planet.retrograde) {
                sb.append(" in stazione retrograda")
            }
            sb.append("\n")
        }

        horoscopeParams.conjunctions.forEach { conjunction ->
            val planetA = conjunction[0]
            val planetB = conjunction[1]
            sb.append("- ${planetA.planet.nome} è in congiunzione con ${planetB.planet.nome}\n")
        }

        horoscopeParams.oppositions.forEach { opposition ->
            val planetA = opposition[0]
            val planetB = opposition[1]
            sb.append("- ${planetA.planet.nome} è in opposizione con ${planetB.planet.nome}\n")
        }

        if (horoscopeParams.planets.isEmpty()) {
            sb.append("- Non sono presenti pianeti nel segno")
        }

        return sb.toString()
    }
}