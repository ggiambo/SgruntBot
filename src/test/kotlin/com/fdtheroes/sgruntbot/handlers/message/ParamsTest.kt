package com.fdtheroes.sgruntbot.handlers.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ParamsTest {

    @Test
    fun testPlanetName() {
        val names = (0..9).map {
            planetName(it)
        }

        assertThat(names).containsExactly(
            "Il sole",
            "La luna",
            "Mercurio",
            "Venere",
            "Marte",
            "Giove",
            "Saturno",
            "Urano",
            "Nettuno",
            "Plutone")
    }
}