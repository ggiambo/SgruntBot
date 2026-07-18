package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.whenever

class MeteoTest : BaseTest() {

    private val meteo = Meteo(botUtils, botConfig)

    init {
        doAnswer {
            val url = it.arguments[0] as String
            val citta = url.substringAfterLast('/')
            "$citta: 🌤️  🌡️+25°C 🌬️→7km/h".byteInputStream()
        }.whenever(botUtils).streamFromURL(
            anyString(),
            anyOrNull<List<Pair<String, String>>>(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
            anyOrNull(),
        )
    }

    @Test
    fun test_meteo() {
        meteo.handle(message("!meteo CasaMia"))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().message).isEqualTo("CasaMia: 🌤️  🌡️+25°C 🌬️→7km/h")
    }

    @Test
    fun test_noMeteo() {
        meteo.handle(message("!meteo"))

        assertThat(actionResponses).isEmpty()
    }

    @Test
    fun test_metei() {
        meteo.handle(message("!metei"))

        assertThat(actionResponses).hasSize(1)
        assertThat(actionResponses.first().message).isEqualTo(
            """
            Genova: 🌤️  🌡️+25°C 🌬️→7km/h
            Guidonia: 🌤️  🌡️+25°C 🌬️→7km/h
            Kollbrunn: 🌤️  🌡️+25°C 🌬️→7km/h
            Legnano: 🌤️  🌡️+25°C 🌬️→7km/h
            Pisa: 🌤️  🌡️+25°C 🌬️→7km/h
            Tradate: 🌤️  🌡️+25°C 🌬️→7km/h
        """.trimIndent()
        )
    }

}