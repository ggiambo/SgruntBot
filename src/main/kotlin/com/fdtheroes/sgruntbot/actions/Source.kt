package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Source : Action, HasHalp {

    private val regex = Regex("^!source\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.containsMatchIn(message.text)) {
            return doNext(ActionResponse.message("http://github.com/ggiambo/SgruntBot"))
        }
    }

    override fun halp() = "<b>!source</b> mostra il sorgente di Sgrunty"

}
