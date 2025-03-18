package com.fdtheroes.sgruntbot.handlers.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class LLMTest {

    private val llm = spy<LLM>(LLM()) {
        onGeneric { callLLM(any()) } doAnswer {
            val input: String = it.component1()
            "call LLM: '$input"
        }
    }

    @Test
    fun testInvalidSign() {
        val horoscope = llm.getHoroscope("ofiuco")
        assertThat(horoscope).isEqualTo("Invalid sign name.")
    }

    @Test
    fun testAriete() {
        val horoscope = llm.getHoroscope("aRiEtE")
        assertThat(horoscope).startsWith("call LLM: 'Sei un astrologo, produci un oroscopo per l'Ariete in un breve paragrafo nello stile di Branko. Di seguito una lista di parametri rilevanti per l'oroscopo di oggi di questo segno. Non citare pianeti che non sono nella lista.")
    }

}