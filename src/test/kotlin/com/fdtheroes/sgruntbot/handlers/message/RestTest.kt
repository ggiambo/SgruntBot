package com.fdtheroes.sgruntbot.handlers.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RestTest {

    private val id = Rest("http://test.tsts", "/url/to/swagger")

    @Test
    fun testPositive() {
        val halp = id.halp()

        assertThat(halp).isEqualTo("<b>http://test.tsts/url/to/swagger</b> per una descrizione dei metodi dell'interfaccia REST")
    }

}