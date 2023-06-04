package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

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

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            doNext(ActionResponse.message(risposte.random()))
        }
    }
}
