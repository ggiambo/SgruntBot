package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import tools.jackson.databind.json.JsonMapper
import java.io.File

@Service
class LobsteRs(botUtils: BotUtils, botConfig: BotConfig, private val jsonMapper: JsonMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!lr$")

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botUtils.sgruntyScrive(message.chatId.toString())
            val messageContent = getMessageContent()
            botUtils.rispondi(ActionResponse.message(messageContent), message)
        }
    }

    fun getMessageContent(): String {
        return Jsoup.parse(File("lobsters.html"))
            .select(".details a.u-url")
            .take(10)
            .joinToString(
                separator = "\n",
                prefix = "Lobste.rs Top 10 Stories:\n"
            ) { "\uD83E\uDD9E <a href=\"${it.text()}\">${it.attr("href")}</a>" }
    }

    override fun halp() = "<b>!lr</b> Top 10 Lobste.rs Stories"

}
