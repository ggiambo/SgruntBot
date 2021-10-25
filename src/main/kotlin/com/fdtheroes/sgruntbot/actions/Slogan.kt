package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message
import java.net.URL

class Slogan : Action {

    private val regex = Regex("^!slogan (.*)\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null) {
            val slogan = fetchSlogan(testo)
            BotUtils.instance.rispondiAsText(message, slogan)
        }
    }

    companion object {
        fun fetchSlogan(testo: String): String {
            val res = BotUtils.instance.textFromURL("http://www.sloganizer.net/en/outbound.php?slogan=${testo}")
            return Regex("<a.*?>(.*)</a>").find(res)?.groupValues?.get(1).orEmpty()
        }
    }

}