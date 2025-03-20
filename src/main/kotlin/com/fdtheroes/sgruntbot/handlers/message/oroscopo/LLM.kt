package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class LLM(@Value("\${GEMINI_API_KEY}") geminiApiKey: String) {

    private val model = GoogleAiGeminiChatModel.builder()
        .apiKey(geminiApiKey)
        .modelName("gemini-2.0-flash")
        .build()

    @Cacheable(cacheNames = ["HoroscopeCache"], keyGenerator = "HoroscopeKeyGenerator")
    fun getHoroscope(zodiacSign: Sign): String {

        val horoscopeParams = getHoroscopeParams(zodiacSign)
        val signNameWithPreposition = zodiacSign.getSignNameWithPreposition("")
        val horoscopeString = horoscopeParams.toStringInItalian()

        val prompt =
            "Sei un astrologo, produci un oroscopo per $signNameWithPreposition in un breve paragrafo nello stile di Branko. Di seguito una lista di parametri rilevanti per l'oroscopo di oggi di questo segno. Non citare pianeti che non sono nella lista.\n$horoscopeString"
        val result = model.chat(prompt)

        return result
    }

}