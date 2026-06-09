package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.readValue
import java.time.ZonedDateTime

@Service
class RedditppGnius(botUtils: BotUtils, botConfig: BotConfig, private val jsonMapper: JsonMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("!gnius$", RegexOption.IGNORE_CASE)
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val defaultSubreddits = arrayOf("linux", "netsec", "programming", "technology", "privacy")
    private val defaultBody = object {
        val sort = "TOP"
        val time = "DAY"
    }

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val gnius = getGnius()
            if (gnius.isNotEmpty()) {
                botUtils.rispondi(ActionResponse.message(gnius.joinToString("\n")), message)
            }
        }
    }

    override fun halp() = "<b>!gnius</b> News sul mondo GNU e altro."

    fun getGnius(vararg subReddits: String): List<String> {
        val gnius = if (subReddits.isEmpty()) {
            defaultSubreddits.flatMap { fetch(it) }
        } else {
            subReddits.flatMap { fetch(it) }
        }
        return gnius
            .sortedByDescending { it.score }
            .take(10)
            .map { gnius(it) }
    }

    private fun fetch(subreddit: String): List<Gnius.Post> {
        val redditNews = try {
            botUtils.textFromURL(
                url = "https://redditpp.com/api/subreddit/$subreddit",
                headers = listOf(Pair(HttpHeaders.USER_AGENT, botConfig.botName)),
                body = defaultBody
            )
        } catch (e: Exception) {
            log.error("Reddit mi odia", e)
            return emptyList()
        }

        return jsonMapper.readValue<Gnius>(redditNews).posts
    }

    private fun gnius(post: Gnius.Post): String {
        val title = botUtils.trimString(post.title, 90)
        val href = "<a href='${post.link}'>$title</a>"
        return "\uD83D\uDCF0 - $href"
    }

    private class Gnius(val posts: List<Post>) {
        class Post(
            val score: Int,
            val title: String,
            permalink: String,
        ) {
            val link = "https://reddit.com/$permalink"
        }
    }

}
