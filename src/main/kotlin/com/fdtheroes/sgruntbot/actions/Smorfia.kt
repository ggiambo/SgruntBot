package com.fdtheroes.sgruntbot.actions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import kotlin.random.Random.Default.nextInt

@Service
class Smorfia : Action {

    private val regex = Regex("\\b(\\d{1,2})\\b", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val smorfiaType = TypeFactory.defaultInstance()
        .constructMapType(Map::class.java, Int::class.java, String::class.java)

    private val smorfia: Map<Int, String> by lazy {
        ObjectMapper().readValue(this::class.java.getResourceAsStream("/smorfia.json"), smorfiaType)
    }

    override fun doAction(ctx: ActionContext) {
        if (nextInt(50) == 0) {
            val numero = getNumero(ctx.message.text)
            if (numero != null) {
                val testoSmorfia = smorfia[numero]
                if (testoSmorfia != null) {
                    ctx.addResponse(ActionResponse.message(testoSmorfia))
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
