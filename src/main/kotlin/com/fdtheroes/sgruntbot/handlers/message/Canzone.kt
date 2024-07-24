package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.urlEncode
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.io.InputStream
import java.net.URL

@Service
class Canzone(
    botUtils: BotUtils,
    botConfig: BotConfig,
    val mapper: ObjectMapper,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val instanceUrl by lazy { initInstanceUrl() }
    private val regex = Regex("!canzone (.*)$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val canzone = regex.find(message.text)?.groupValues?.get(1)
        if (canzone != null) {
            val titleAndVideoId = getTitleAndVideoId(canzone)
            if (titleAndVideoId.first == null || titleAndVideoId.second == null) {
                botUtils.rispondi(ActionResponse.message("Non ci riesco."), message)
                return
            }
            val title = titleAndVideoId.first!!
            val videoId = titleAndVideoId.second!!
            val file = download(videoId)

            val audio = InputFile(file, title)
            botUtils.rispondi(ActionResponse.audio(title, audio), message)
        }

    }

    private fun download(videoId: String): InputStream {
        val videosUrl = "$instanceUrl/api/v1/videos/$videoId"
        val textFromURL = botUtils.textFromURL(videosUrl)
        val url = mapper.readTree(textFromURL)["adaptiveFormats"][0]["url"].textValue()
        val videoUrl = URL(url)
        val downloadUrl = "${instanceUrl}${videoUrl.path}?${videoUrl.query}"
        return botUtils.streamFromURL(downloadUrl)
    }

    private fun getTitleAndVideoId(query: String): Pair<String?, String?> {
        val searchUrl = "$instanceUrl/api/v1/search?q=${query.urlEncode()}&type=video&region=IT&sort=relevance"
        val textFromURL = botUtils.textFromURL(searchUrl)
        val firstEntry = mapper.readTree(textFromURL)[0]
        return Pair(firstEntry["title"].textValue(), firstEntry["videoId"].textValue())
    }

    private fun initInstanceUrl(): String {
        val textFromURL = botUtils.textFromURL("https://api.invidious.io/instances.json")
        return mapper.readTree(textFromURL)[0][1]["uri"].textValue()
    }

    override fun halp() = "<b>!canzone</b> <i>la tua canzone</i> cerca e scarica la tua canzone"
}