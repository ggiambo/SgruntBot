package com.fdtheroes.sgruntbot.handlers.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.urlEncode
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.io.InputStream
import java.net.URI

@Service
class Canzone(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val mapper: ObjectMapper,
    private val canzoneCache: CanzoneCache,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("!canzone (.*)$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val canzone = regex.find(message.text)?.groupValues?.get(1)
        if (canzone != null) {
            CoroutineScope(Dispatchers.Default).launch {
                botUtils.sgruntyScrive(botConfig.chatId, ActionType.UPLOAD_VOICE)
            }
            val (title, videoId) = getTitleAndVideoId(canzone)
            if (title == null || videoId == null) {
                botUtils.rispondi(ActionResponse.message("Non ci riesco."), message)
                return
            }
            val (video, thumbnail) = videoAndThumbnail(videoId, title)
            botUtils.rispondi(ActionResponse.audio(title, video, thumbnail), message)
        }
    }

    private fun videoAndThumbnail(videoId: String, title: String): Pair<InputFile, InputFile> {
        val instanceUrl = try {
            canzoneCache.initInstanceUrl()
        } catch (e: Exception) {
            botUtils.messaggio(ActionResponse.message("Non ce la faccio a trovare un sito funzionante: ${e.message}"))
            throw e
        }

        val videosUrl = "$instanceUrl/api/v1/videos/$videoId"

        val textFromURL = try {
            botUtils.textFromURL(videosUrl)
        } catch (e: Exception) {
            botUtils.messaggio(ActionResponse.message("Non ce la faccio leggere da ${videosUrl}: ${e.message}"))
            throw e
        }

        val content = mapper.readTree(textFromURL)

        val (videoInputStream, thumbnailInputStream) = runBlocking {
            awaitAll(getVideo(content), getThumbnail(content))
        }

        val video = InputFile(videoInputStream, title)
        val thumbnail = InputFile(thumbnailInputStream, "thumbnail.jpg")

        return Pair(video, thumbnail)
    }

    private fun getVideo(content: JsonNode): Deferred<InputStream> {
        val url = content["adaptiveFormats"][0]["url"].textValue()
        val videoUrl = URI(url)
        val instanceUrl = canzoneCache.initInstanceUrl()
        val downloadUrl = "${instanceUrl}${videoUrl.path}?${videoUrl.query}"
        return CoroutineScope(Dispatchers.Default).async {
            botUtils.streamFromURL(downloadUrl)
        }
    }

    private fun getThumbnail(content: JsonNode): Deferred<InputStream?> {
        val url = content["videoThumbnails"].toList().firstOrNull {
            it["quality"].textValue() == "default"
        }
        if (url == null) {
            return CoroutineScope(Dispatchers.Default).async { null }
        }
        return CoroutineScope(Dispatchers.Default).async {
            botUtils.streamFromURL(url["url"].textValue())
        }
    }

    private fun getTitleAndVideoId(query: String): Pair<String?, String?> {
        val instanceUrl = canzoneCache.initInstanceUrl()
        val searchUrl = "$instanceUrl/api/v1/search?q=${query.urlEncode()}&type=video&region=IT&sort=relevance"
        val textFromURL = botUtils.textFromURL(searchUrl)
        val firstEntry = mapper.readTree(textFromURL)[0]
        return Pair(firstEntry["title"].textValue(), firstEntry["videoId"].textValue())
    }

    override fun halp() = "<b>!canzone</b> <i>la tua canzone</i> cerca e scarica la tua canzone"

}

@Service
class CanzoneCache(private val botUtils: BotUtils, private val mapper: ObjectMapper) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Cacheable("invidious")
    fun initInstanceUrl(): String {
        log.info("Fetching invidious instance url")
        val textFromURL = botUtils.textFromURL("https://api.invidious.io/instances.json?pretty=1&sort_by=type,users")
        val validUrls = mapper.readTree(textFromURL).firstNotNullOf {
            val url = it[1]["uri"].textValue()
            try {
                log.info("Testing $url")
                val response = botUtils.textFromURL("$url/api/v1/videos/CK4fzjY6Elo") // test se veramente funziona
                if (response.contains("Linux su desktop è al massimo storico")) {
                    log.info("$url sembra OK!")
                    return@firstNotNullOf url
                }
                return@firstNotNullOf null
            } catch (e: Exception) {
                log.info("$url NOK: ${e.message}")
                return@firstNotNullOf null
            }
        }
        return validUrls
    }

}