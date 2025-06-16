package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class LLM(
    @Value("\${GEMINI_API_KEY}") geminiApiKey: String,
    private val horoscopeUtils: HoroscopeUtils,
    private val botUtils: BotUtils,
    private val objectMapper: ObjectMapper,
) {

    private val headers = listOf(
        "x-goog-api-key" to geminiApiKey,
        "Content-Type" to "application/json",
        "User-Agent" to "LangChain4j"
    )

    @Cacheable(cacheNames = ["horoscopeCache"], keyGenerator = "HoroscopeKeyGenerator")
    fun getHoroscope(zodiacSign: Sign): String {

        val horoscopeParams = horoscopeUtils.getHoroscopeParams(zodiacSign)
        val signNameWithPreposition = zodiacSign.description()
        val horoscopeString = horoscopeUtils.toStringInItalian(horoscopeParams)

        val prompt =
            "Sei un astrologo, produci un oroscopo per $signNameWithPreposition in un breve paragrafo nello stile di Branko. Di seguito una lista di parametri rilevanti per l'oroscopo di oggi di questo segno. Non citare pianeti che non sono nella lista.\n$horoscopeString"
        val request = Request(prompt)
        val response = botUtils.textFromURL(
            url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent",
            headers = headers,
            body = request
        )

        return objectMapper.readTree(response)["candidates"].first()["content"]["parts"].first()["text"].asText()
    }

}