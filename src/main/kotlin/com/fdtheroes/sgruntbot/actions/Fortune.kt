package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Fortune : Action, HasHalp {

    private val regex = Regex("^!(fortune|quote)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            doNext(ActionResponse.message(getFortune()))
        }
    }

    override fun halp() = "<b>!fortune</b> oppure <b>!quote</b> per una perla di saggezza"

    fun getFortune(): String {
        return Runtime.getRuntime().exec("fortune -sa it")
            .inputStream
            .reader()
            .readText()
    }

}
