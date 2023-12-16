package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random.Default.nextInt

@Service
class Smorfia(val mapper: ObjectMapper) : Action {

    private val regex = Regex("\\b(\\d{1,2})\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    private val smorfia: Map<Int, String> by lazy {
        mapper.readTree(this::class.java.getResourceAsStream("/smorfia.json"))
            .associate { Pair(it["n"].asInt(), it["text"].asText()) }
    }

    // ogni 3 messaggi, risponde con la smorfia
    var contatore = AtomicInteger(0)

    override fun doAction(ctx: ActionContext) {
        val numero = getNumero(ctx.message.text)
        if (numero != null) {
            val testoSmorfia = smorfia[numero]
            if (testoSmorfia != null) {
                if (contatore.incrementAndGet() == 3) {
                    contatore.set(0)
                    ctx.addResponse(ActionResponse.message("\uD83C\uDDEE\uD83C\uDDF9 $numero: $testoSmorfia \uD83E\uDD0C"))
                }
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
