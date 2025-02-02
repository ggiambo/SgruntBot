package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.eclipse.jgit.api.Git
import org.springframework.stereotype.Service
import org.springframework.web.util.HtmlUtils
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset


@Service
class Git(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("!git (\\d+)", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val groupValues = regex.find(message.text)?.groupValues
        if (groupValues == null) {
            return
        }

        val commits = groupValues[1].toInt()
        val messages = Git.open(File("."))
            .log()
            .setMaxCount(commits)
            .call()
            .map {
                val commitTime = LocalDateTime.ofEpochSecond(it.commitTime.toLong(), 0, ZoneOffset.UTC)
                "${commitTime}: ${it.fullMessage}"
            }

        createChunks(messages).forEach {
            botUtils.rispondi(ActionResponse.message(it), message)
        }
    }

    private fun createChunks(allEntries: List<String>): List<String> {
        val chunks = mutableListOf<String>()
        var currentChunk = StringBuilder()

        for (entry in allEntries) {
            if (currentChunk.length + entry.length > 4096) {
                chunks.add(currentChunk.toString())
                currentChunk = StringBuilder()
            }
            
            currentChunk.append(entry)
        }

        if (currentChunk.isNotEmpty()) {
            chunks.add(HtmlUtils.htmlEscape(currentChunk.toString()))
        }

        return chunks
    }

    override fun halp() = "<b>!git <i>numero</i></b>: Mostra gli ultimi <i>numero</i> commit"
}
