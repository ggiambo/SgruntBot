package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Vocale(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val fileName = "LaVoceDiSgrunty.mp3"
    private val url = "https://www.voicerss.org/controls/speech.ashx?hl=it-it&v=%s&src=%s"
    private val regex = Regex(
        "^!vocale (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )
    private val headers = listOf(
        Pair(HttpHeaders.USER_AGENT, botConfig.botName),
        Pair(HttpHeaders.REFERER, "https://www.voicerss.org/api/demo.aspx"),
    )
    private val voce = listOf("Bria", "Mia", "Pietro")

    // curl 'https://www.voicerss.org/controls/speech.ashx?hl=it-it&v=Mia&src=Das%20ist%20kaum%20zum%20glauben&c=mp3&rnd=0.42' -H 'User-Agent: Sgruntbot' -H 'Referer: https://www.voicerss.org/api/demo.aspx' --output audio.mp3
    override fun handle(message: Message) {
        val msg = regex.find(message.text)?.groupValues?.get(1)
        if (msg != null) {
            val laVoceDiSgrunty = getVocale(msg)
            botUtils.rispondi(ActionResponse.audio(fileName, laVoceDiSgrunty), message)
        }
    }

    fun getVocale(testo: String): InputFile {
        val audio = botUtils.streamFromURL(
            url,
            listOf(voce.random(), testo),
            headers
        )

        return InputFile(audio, fileName)
    }

    override fun halp(): String {
        return "<b>!vocale</b> <i>testo</i> Sgrunty ci parla!"
    }
}