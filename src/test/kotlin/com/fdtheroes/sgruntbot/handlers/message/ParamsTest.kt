package com.fdtheroes.sgruntbot.handlers.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class ParamsTest {

    @Test
    fun testGetHoroscopeParams_due_pianeti() {
        val horoscopeParams = getHoroscopeParams(3, LocalDate.of(2024, 7, 9))
        assertThat(horoscopeParams.sign).isEqualTo(3)
        assertThat(horoscopeParams.planets).hasSize(2)
        assertThat(horoscopeParams.conjunctions).isEmpty()
        assertThat(horoscopeParams.oppositions).isEmpty()

        val planetPosition1 = horoscopeParams.planets[0]
        assertThat(planetPosition1.longitude).isEqualTo(109.25254393157307)
        assertThat(planetPosition1.sign).isEqualTo(3)
        assertThat(planetPosition1.longitudeIntoSign).isEqualTo(19.252543931573072)
        assertThat(planetPosition1.planet).isEqualTo(0)
        assertThat(planetPosition1.retrograde).isEqualTo(false)
        assertThat(planetPosition1.enteringSign).isEqualTo(-1)
        assertThat(planetPosition1.leavingSign).isEqualTo(-1)
        assertThat(planetPosition1.moonPhase).isEqualTo(null)

        val planetPosition2 = horoscopeParams.planets[1]
        assertThat(planetPosition2.longitude).isEqualTo(119.62553851704686)
        assertThat(planetPosition2.sign).isEqualTo(3)
        assertThat(planetPosition2.longitudeIntoSign).isEqualTo(29.625538517046863)
        assertThat(planetPosition2.planet).isEqualTo(3)
        assertThat(planetPosition2.retrograde).isEqualTo(false)
        assertThat(planetPosition2.enteringSign).isEqualTo(4)
        assertThat(planetPosition2.leavingSign).isEqualTo(3)
        assertThat(planetPosition2.moonPhase).isEqualTo(null)

        assertThat(horoscopeParams.toStringInItalian()).isEqualTo("""
              - Il sole è nel Cancro
              - Venere sta uscendo da dal Cancro
              
        """.trimIndent())
    }

    @Test
    fun testGetHoroscopeParams_congiunzione() {
        val horoscopeParams = getHoroscopeParams(6, LocalDate.of(2024, 9, 5))
        assertThat(horoscopeParams.sign).isEqualTo(6)
        assertThat(horoscopeParams.planets).hasSize(2)
        assertThat(horoscopeParams.oppositions).isEmpty()
        assertThat(horoscopeParams.conjunctions).hasSize(1)

        val conjunction = horoscopeParams.conjunctions[0]
        assertThat(conjunction).hasSize(2)

        val conjuctionPlanetPosition1 = conjunction[0]
        assertThat(conjuctionPlanetPosition1.longitude).isEqualTo(188.81831146242044)
        assertThat(conjuctionPlanetPosition1.sign).isEqualTo(6)
        assertThat(conjuctionPlanetPosition1.longitudeIntoSign).isEqualTo(8.81831146242044)
        assertThat(conjuctionPlanetPosition1.planet).isEqualTo(1)
        assertThat(conjuctionPlanetPosition1.retrograde).isEqualTo(false)
        assertThat(conjuctionPlanetPosition1.enteringSign).isEqualTo(-1)
        assertThat(conjuctionPlanetPosition1.leavingSign).isEqualTo(-1)
        assertThat(conjuctionPlanetPosition1.moonPhase).isEqualTo("crescente")

        val conjuctionPlanetPosition2 = conjunction[1]
        assertThat(conjuctionPlanetPosition2.longitude).isEqualTo(188.15087135886387)
        assertThat(conjuctionPlanetPosition2.sign).isEqualTo(6)
        assertThat(conjuctionPlanetPosition2.longitudeIntoSign).isEqualTo(8.150871358863867)
        assertThat(conjuctionPlanetPosition2.planet).isEqualTo(3)
        assertThat(conjuctionPlanetPosition2.retrograde).isFalse()
        assertThat(conjuctionPlanetPosition2.enteringSign).isEqualTo(-1)
        assertThat(conjuctionPlanetPosition2.leavingSign).isEqualTo(-1)
        assertThat(conjuctionPlanetPosition2.moonPhase).isNull()


        val planetPosition1 = horoscopeParams.planets[0]
        assertThat(planetPosition1.longitude).isEqualTo(188.81831146242044)
        assertThat(planetPosition1.sign).isEqualTo(6)
        assertThat(planetPosition1.longitudeIntoSign).isEqualTo(8.81831146242044)
        assertThat(planetPosition1.planet).isEqualTo(1)
        assertThat(planetPosition1.retrograde).isFalse()
        assertThat(planetPosition1.enteringSign).isEqualTo(-1)
        assertThat(planetPosition1.leavingSign).isEqualTo(-1)
        assertThat(planetPosition1.moonPhase).isEqualTo("crescente")

        val planetPosition2 = horoscopeParams.planets[1]
        assertThat(planetPosition2.longitude).isEqualTo(188.15087135886387)
        assertThat(planetPosition2.sign).isEqualTo(6)
        assertThat(planetPosition2.longitudeIntoSign).isEqualTo(8.150871358863867)
        assertThat(planetPosition2.planet).isEqualTo(3)
        assertThat(planetPosition2.retrograde).isEqualTo(false)
        assertThat(planetPosition2.enteringSign).isEqualTo(-1)
        assertThat(planetPosition2.leavingSign).isEqualTo(-1)
        assertThat(planetPosition2.moonPhase).isEqualTo(null)

        assertThat(horoscopeParams.toStringInItalian()).isEqualTo("""
                - La luna crescente è nella Bilancia
                - Venere è nella Bilancia
                - La luna è in congiunzione con Venere
                
                """.trimIndent())
    }

    @Test
    fun testGetHoroscopeParams_opposizione() {
        val horoscopeParams = getHoroscopeParams(6, LocalDate.of(2024, 10, 17))
        assertThat(horoscopeParams.sign).isEqualTo(6)
        assertThat(horoscopeParams.planets).hasSize(1)
        assertThat(horoscopeParams.oppositions).hasSize(1)
        assertThat(horoscopeParams.conjunctions).isEmpty()

        val oppositions = horoscopeParams.oppositions[0]
        assertThat(oppositions).hasSize(2)

        val oppositionPlanetPosition1 = oppositions[0]
        assertThat(oppositionPlanetPosition1.longitude).isEqualTo(202.79133178108123)
        assertThat(oppositionPlanetPosition1.sign).isEqualTo(6)
        assertThat(oppositionPlanetPosition1.longitudeIntoSign).isEqualTo(22.791331781081226)
        assertThat(oppositionPlanetPosition1.planet).isEqualTo(0)
        assertThat(oppositionPlanetPosition1.retrograde).isEqualTo(false)
        assertThat(oppositionPlanetPosition1.enteringSign).isEqualTo(-1)
        assertThat(oppositionPlanetPosition1.leavingSign).isEqualTo(-1)
        assertThat(oppositionPlanetPosition1.moonPhase).isNull()

        val oppositionPlanetPosition2 = oppositions[1]
        assertThat(oppositionPlanetPosition2.longitude).isEqualTo(22.47443106598022)
        assertThat(oppositionPlanetPosition2.sign).isEqualTo(0)
        assertThat(oppositionPlanetPosition2.longitudeIntoSign).isEqualTo(22.47443106598022)
        assertThat(oppositionPlanetPosition2.planet).isEqualTo(1)
        assertThat(oppositionPlanetPosition2.retrograde).isFalse()
        assertThat(oppositionPlanetPosition2.enteringSign).isEqualTo(-1)
        assertThat(oppositionPlanetPosition2.leavingSign).isEqualTo(-1)
        assertThat(oppositionPlanetPosition2.moonPhase).isEqualTo("piena")

        val planetPosition = horoscopeParams.planets[0]
        assertThat(planetPosition.longitude).isEqualTo(202.79133178108123)
        assertThat(planetPosition.sign).isEqualTo(6)
        assertThat(planetPosition.longitudeIntoSign).isEqualTo(22.791331781081226)
        assertThat(planetPosition.planet).isEqualTo(0)
        assertThat(planetPosition.retrograde).isFalse()
        assertThat(planetPosition.enteringSign).isEqualTo(-1)
        assertThat(planetPosition.leavingSign).isEqualTo(-1)
        assertThat(planetPosition.moonPhase).isNull()

        assertThat(horoscopeParams.toStringInItalian()).isEqualTo("""
            - Il sole è nella Bilancia
            - Il sole è in opposizione con La luna
  
        """.trimIndent())
    }
}