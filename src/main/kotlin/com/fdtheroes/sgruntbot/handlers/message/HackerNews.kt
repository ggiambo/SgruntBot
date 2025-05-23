package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class HackerNews(botUtils: BotUtils, botConfig: BotConfig, private val mapper: ObjectMapper) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!hn$")

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botUtils.sgruntyScrive(message.chatId.toString())
            val topStories = botUtils.textFromURL("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty")
            val messageContent = mapper.readTree(topStories)
                .take(10)
                .joinToString(separator = "\n", prefix = "Hacker News Top Stories:\n") {
                    fetchStory(it.asInt())
                }
            botUtils.rispondi(ActionResponse.message(messageContent), message)
        }
    }

    private fun fetchStory(id: Int): String {
        val story = botUtils.textFromURL("https://hacker-news.firebaseio.com/v0/item/$id.json?print=pretty")
        val title = mapper.readTree(story)["title"].asText()
        val url = mapper.readTree(story)["url"]?.asText() ?: "https://news.ycombinator.com/item?id=$id"
        return "\uD83D\uDCE2 <a href=\"$url\">$title</a>"
    }

    override fun halp() = "<b>!hn</b> Top 10 Hacker News Stories"
}