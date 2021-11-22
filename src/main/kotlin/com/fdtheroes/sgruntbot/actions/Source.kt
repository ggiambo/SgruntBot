package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Source : Action, HasHalp {

    private val regex = Regex("^!source\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            return BotUtils.rispondiAsText(message, "http://github.com/ggiambo/SgruntBot")
        }
    }

    override fun halp() = "<b>!source</b> mostra il sorgente di Sgrunty"

}