package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.Gnius
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.net.InetSocketAddress
import java.net.Proxy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Service
class RedditGnius(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("!gnius$", RegexOption.IGNORE_CASE)
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val torProxy = Proxy(Proxy.Type.SOCKS, InetSocketAddress("localhost", 9050))

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val gnius = getGnius()
            if (gnius.isNotEmpty()) {
                botUtils.rispondi(ActionResponse.message(gnius.joinToString("\n")), message)
            }
        }
    }

    override fun halp() = "<b>!gnius</b> News sul mondo GNU e altro."

    fun getGnius(): List<String> {
        return fetch().map { gnius(it) }
    }

    private fun fetch(): List<Gnius> {
        val redditNews = try {
            botUtils.textFromURL(
                url = "https://old.reddit.com/r/linux+netsec+programming+technology+privacy/top/",
                headers = listOf(Pair(HttpHeaders.USER_AGENT, botConfig.botName)),
                proxy = torProxy
            )
        } catch (e: Exception) {
            log.error("Reddit mi odia", e)
            return emptyList()
        }

        return Jsoup.parse(redditNews)
            .select(".thing > .entry")
            .mapNotNull { entry ->
                val dateTime = entry.select("time").attr("datetime")
                if (dateTime.isEmpty()) {
                    return@mapNotNull null
                }
                val titleLink = entry.select("a.title")
                Gnius(
                    published = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    title = titleLink.text(),
                    link = titleLink.attr("href"),
                )
            }
            .take(10)
    }

    private fun gnius(gnius: Gnius): String {
        val title = botUtils.trimString(gnius.title, 90)
        val href = "<a href='${gnius.link}'>$title</a>"
        return "\uD83D\uDCF0 - $href"
    }
}
