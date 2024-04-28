package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.Gnius
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.dateTime
import org.slf4j.LoggerFactory
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.function.ServerRequest
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class RedditGnius(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("!gnius$", RegexOption.IGNORE_CASE)
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val xmlMapper = Jackson2ObjectMapperBuilder
        .xml()
        .build<ObjectMapper>()

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val gnius = fetch()
            if (gnius.isNotEmpty()) {
                val gniusMessage = gnius.joinToString(separator = "\n") { gnius(it) }
                botUtils.rispondi(ActionResponse.message(gniusMessage), message)
            }
        }
    }

    override fun halp() = "<b>!gnius</b> News sul mondo GNU e altro."

    private fun fetch(): List<Gnius> {
        val redditNews = try {
            botUtils.textFromURL(
                url = "https://old.reddit.com/r/linux+netsec+programming+technology/top/.rss",
                headers = listOf(Pair("User-Agent", botConfig.botName))
            )
        } catch (e: Exception) {
            log.error("Reddit mi odia", e)
            return emptyList()
        }

        return xmlMapper
            .readTree(redditNews)["entry"]
            .map {
                Gnius(
                    updated = it["updated"].dateTime(),
                    published = it["published"].dateTime(),
                    title = it["title"].textValue(),
                    link = it["link"]["href"].textValue(),
                )
            }
            .sortedByDescending { it.published }
            .take(5)
    }

    private fun gnius(gnius: Gnius): String {
        val title = botUtils.trimString(gnius.title, 80)
        val href = "<a href='${gnius.link}'>$title</a>"
        return "\uD83D\uDCF0 - $href"
    }
}
