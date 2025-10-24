package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import tools.jackson.databind.json.JsonMapper
import java.net.InetSocketAddress
import java.net.Proxy

@Service
class LLM(
    @Value("\${GEMINI_API_KEY}") geminiApiKey: String,
    private val horoscopeUtils: HoroscopeUtils,
    private val botUtils: BotUtils,
    private val jsonMapper: JsonMapper,
) {

    private val suoraProxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("198.98.49.55", 8118))

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
            body = request,
            proxy = suoraProxy
        )

        return jsonMapper.readTree(response)["candidates"].first()["content"]["parts"].first()["text"].asString()
    }

}