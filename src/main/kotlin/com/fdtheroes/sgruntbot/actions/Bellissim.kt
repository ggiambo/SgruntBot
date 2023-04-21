package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random
import kotlin.random.Random.Default.nextBoolean

@Service
class Bellissim : Action {

    private val regex = Regex("bellissim", RegexOption.IGNORE_CASE)

    val risposte = listOf(
        "IO sono bellissimo! .... anzi stupendo! fantastico! eccezionale!",
        "IO sono bellissimo! .... vabbé, facciamo a turni.",
        "IO sono bellissimo! .... quasi bello come Giambo <3",
        "IO sono bellissimo! .... tu sei brutto come il culo di un cane da caccia",
        "IO sono bellissimo! .... quante volte devo ripetermi?",
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            if (Random.nextInt(5) == 0) {
                sgruntBot.rispondi(message, risposte.random())
            }
        }
    }
}
