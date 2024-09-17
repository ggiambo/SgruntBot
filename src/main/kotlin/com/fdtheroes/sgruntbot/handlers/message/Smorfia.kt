package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random.Default.nextInt

@Service
class Smorfia(botUtils: BotUtils, botConfig: BotConfig, val mapper: ObjectMapper) :
    MessageHandler(botUtils, botConfig) {

    private val regex = Regex("\\b(\\d{1,2})\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val regexRichiesta = Regex("!smorfia \\d{1,2}", setOf(RegexOption.IGNORE_CASE))

    private val smorfia: Map<Int, String> by lazy {
        mapper.readTree(this::class.java.getResourceAsStream("/smorfia.json"))
            .associate { Pair(it["n"].asInt(), it["text"].asText()) }
    }

    override fun handle(message: Message) {
        val numero = getNumero(message.text)
        if (numero != null && nextInt(50) == 0 || regexRichiesta.containsMatchIn(message.text)) {   // 2% di probabilità oppure richiesta secca
            val testoSmorfia = smorfia[numero]
            if (testoSmorfia != null) {
                botUtils.rispondi(
                    ActionResponse.message("\uD83C\uDDEE\uD83C\uDDF9 $numero: $testoSmorfia \uD83E\uDD0C"),
                    message
                )
            }
        }
    }

    fun getNumero(testo: String): Int? {
        val numero = regex.find(testo)?.groupValues?.get(1)
        if (numero != null && numero.toInt() in 1..90) {
            return numero.toInt()
        }
        return null
    }

}
