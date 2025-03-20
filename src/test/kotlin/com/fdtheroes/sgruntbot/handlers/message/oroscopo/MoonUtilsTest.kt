package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDate

class MoonUtilsTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "2021-01-13, nuova",
            "2021-01-28, piena",
            "2021-01-21, crescente",
            "2021-01-07, calante"
        ]
    )
    fun getMoonPhaseString(data: String, expected: String) {
        val moonUtils = MoonUtils()
        val currentDate = LocalDate.parse(data)
        val result = moonUtils.getMoonPhaseString(currentDate)
        assertThat(expected).isEqualTo(result)
    }
}