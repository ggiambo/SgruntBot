package com.fdtheroes.sgruntbot.handlers.message.oroscopo


class Request(prompt: String) {

    val contents = listOf(Content(prompt))
    val generationConfig = GenerationConfig

    object GenerationConfig {
        val responseMimeType = "text/plain"
        val candidateCount = 1
    }

    class Content(prompt: String) {
        val parts = listOf(Part(prompt))
        val role = "user"
    }

    class Part(val text: String)

}