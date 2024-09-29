package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.length
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random.Default.nextInt

@Service
class LaSmorfia(botUtils: BotUtils, botConfig: BotConfig, val mapper: ObjectMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("\\b(\\d{1,2})\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val regexRichiesta = Regex("!smorfia (\\d{1,2})\$", setOf(RegexOption.IGNORE_CASE))

    private val smorfia: List<Smorfia> by lazy {
        mapper.readTree(this::class.java.getResourceAsStream("/smorfia.json"))
            .map {
                Smorfia(
                    numero = it["n"].asText().toInt(),
                    text = it["text"].asText(),
                    keywords = it["w"]?.map { keyword -> keyword.asText() }.orEmpty(),
                )
            }
            .sortedBy { it.numero }
    }

    override fun handle(message: Message) {
        if (regexRichiesta.matches(message.text)) {
            val numero = regexRichiesta.find(message.text)?.groupValues?.get(1)?.toIntOrNull()
            if (numero != null && numero < smorfia.length()) {
                val testoSmorfia = smorfia[numero].text
                botUtils.rispondi(
                    ActionResponse.message("\uD83C\uDDEE\uD83C\uDDF9 $numero: $testoSmorfia \uD83E\uDD0C"),
                    message
                )
            }
        } else if (nextInt(50) == 0) {
            val smorfie = smorfia.flatMap {
                it.keywords.mapNotNull { keyword ->
                    if (message.text.contains(keyword)) Pair(keyword, it) else null
                }
            }
            if (smorfie.isNotEmpty()) {
                val candidato = smorfie.random()
                val keyword = candidato.first
                val smorfia = candidato.second
                botUtils.rispondi(
                    ActionResponse.message("\uD83C\uDDEE\uD83C\uDDF9 ${keyword}: ${smorfia.numero}, ${smorfia.text} \uD83E\uDD0C"),
                    message
                )
            }

        }
    }

    override fun halp() = "<b>!smorfia</b> <i>numero</i> inutilissima smorfia napoletana"

    private class Smorfia(
        val numero: Int,
        val text: String,
        val keywords: List<String> = emptyList(),
    )

}
