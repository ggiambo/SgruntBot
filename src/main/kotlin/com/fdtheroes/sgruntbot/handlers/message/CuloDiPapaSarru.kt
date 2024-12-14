package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.random.ScheduledCuloDiPapaSarru
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class CuloDiPapaSarru(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val scheduledCuloDiPapaSarru: ScheduledCuloDiPapaSarru,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!vangelo", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            createChunks(scheduledCuloDiPapaSarru.getPrevious()).forEach {
                botUtils.rispondi(ActionResponse.message(it), message)
            }
        }
    }

    private fun createChunks(allEntries: List<String>): List<String> {
        val chunks = mutableListOf<String>()
        var currentChunk = StringBuilder()

        for (entry in allEntries) {
            val entryWithBullet = "- $entry\n"
            if (currentChunk.length + entryWithBullet.length > 4096) {
                chunks.add(currentChunk.toString())
                currentChunk = StringBuilder()
            }
            currentChunk.append(entryWithBullet)
        }

        if (currentChunk.isNotEmpty()) {
            chunks.add(currentChunk.toString())
        }

        return chunks
    }

    override fun halp() =
        "<b>!vangelo</b> Il vangelo del CDP Sarru"
}