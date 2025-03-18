package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import dev.langchain4j.model.openai.OpenAiChatModel
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel
import java.time.LocalDate

class LLM {

    val llmType = "gemini"
    private val horoscopeCache = mutableMapOf<String, String>()

    fun getHoroscope(input: String): String {
        val currentDate = LocalDate.now()
        val cacheKey = "$input-${currentDate.year}-${currentDate.monthValue}-${currentDate.dayOfMonth}"

        // Check if the result is already in the cache
        horoscopeCache[cacheKey]?.let {
            return it
        }
        val debugPrefix = "debug:"
        val isDebugMode = input.startsWith(debugPrefix)
        val signInput = if (isDebugMode) input.removePrefix(debugPrefix).trim() else input

        val zodiacSign = Sign.byNome(signInput)
        if (zodiacSign == null) {
            return "Invalid sign name."
        }

        val horoscopeParams = getHoroscopeParams(zodiacSign)
        val result = if (isDebugMode) {
            horoscopeParams.toStringInItalian()
        } else {
            val prompt = "Sei un astrologo, produci un oroscopo per ${
                    zodiacSign.getSignNameWithPreposition("")
            } in un breve paragrafo nello stile di Branko. Di seguito una lista di parametri rilevanti per l'oroscopo di oggi di questo segno. Non citare pianeti che non sono nella lista.\n${horoscopeParams.toStringInItalian()}"
            callLLM(prompt)
        }

        // Store the result in the cache
        horoscopeCache[cacheKey] = result

        return result
    }

    fun callLLM(prompt: String): String {
        val model = when (llmType) {
            "openai" -> OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY")).modelName("gpt-4o").build()
            else -> GoogleAiGeminiChatModel.builder().apiKey(System.getenv("GEMINI_API_KEY"))
                .modelName("gemini-2.0-flash").build()
        }
        return model.chat(prompt)
    }
}