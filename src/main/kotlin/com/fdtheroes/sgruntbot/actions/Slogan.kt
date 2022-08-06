package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.BotUtils.Companion.urlEncode
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

@Service
class Slogan(private val botUtils: BotUtils) : Action, HasHalp {

    private val regex = Regex("^!slogan (.*)\$", RegexOption.IGNORE_CASE)
    private val sloganPlaceholder = "XXX-XXX-XXX"
    private val urlSlogan = "http://www.sloganizer.net/en/outbound.php?slogan=%s"

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null) {
            val slogan = fetchSlogan(testo)
            sgruntBot.rispondiAsText(message, slogan)
        }
    }

    override fun halp() = "<b>!slogan</b> <i>testo</i> uno slogan per il testo!"

    fun fetchSlogan(testo: String): String {
        val res = botUtils.textFromURL(urlSlogan, testo.urlEncode())
        return Regex("<a.*?>(.*)</a>").find(res)?.groupValues?.get(1).orEmpty()
    }

    fun fetchSlogan(utente: User): String {
        val res = fetchSlogan(sloganPlaceholder)
        return res.replace(sloganPlaceholder, botUtils.getUserLink(utente))
    }

}
