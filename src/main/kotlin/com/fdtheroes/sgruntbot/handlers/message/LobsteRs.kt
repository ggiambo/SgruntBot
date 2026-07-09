package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import tools.jackson.databind.json.JsonMapper

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
        val topTen = Jsoup.parse(botUtils.textFromURL("https://lobste.rs"))
            .select(".details")
            .take(10)
        val urls = topTen.map {
            val elem = it.select("a.u-url")
            "\uD83E\uDD9E <a href=\"${elem.attr("href")}\">${elem.text()}</a>"
        }
        val comments = topTen.map {
            val elem = it.select("span.comments_label a")
            "<a href=\"https://lobste.rs/${elem.attr("href")}\">${elem.text()}</a>"
        }

        return urls.zip(comments).joinToString(
            separator = "\n",
            prefix = "Lobste.rs Top 10 Stories:\n"
        ) { "<b>${it.first}</b> | ${it.second}" }
    }

    override fun halp() = "<b>!lr</b> Top 10 Lobste.rs Stories"

}
