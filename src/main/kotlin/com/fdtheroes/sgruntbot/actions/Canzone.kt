package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.pathString

class Canzone : Action {

    private val regex = Regex("!canzone (.*)$", RegexOption.IGNORE_CASE)
    private val destPath: Path

    init {
        val tmpDir = System.getProperty("java.io.tmpdir")
        destPath = Path(tmpDir, "songs")
        if (!destPath.exists()) {
            destPath.createDirectory()
        }
    }

    override fun doAction(message: Message, context: Context) {
        val canzone = regex.find(message.text)?.groupValues?.get(1)
        if (canzone != null) {
            BotUtils.instance.rispondi(SendChatAction(message.chat.id.toString(), ActionType.UPLOADDOCUMENT.toString()))

            val fileName = fetch(canzone)
            if (fileName == null) {
                BotUtils.instance.rispondi(message, "Non ci riesco.")
                return
            }
            val file = destPath.resolve(fileName).toFile()

            val sendAudio = SendAudio()
            sendAudio.chatId = message.chat.id.toString()
            sendAudio.replyToMessageId = message.messageId
            sendAudio.audio = InputFile(file, fileName)

            BotUtils.instance.rispondi(sendAudio)
        }
    }

    private fun fetch(query: String): String? {
        val destDir = destPath.pathString
        val processOutput = ProcessBuilder()
            .command(
                "sh", "-c",
                """youtube-dl --restrict-filenames --extract-audio --audio-format mp3 --output "$destDir/%(title)s.mp3" "ytsearch1:$query" --geo-bypass-country IT 2>&1"""
            )
            .start()
            .inputStream
            .bufferedReader()
            .readText()

        return extractFilename(processOutput)
    }

    private fun extractFilename(processOutput: String): String? {
        val destDir = destPath.pathString
        var fileName = Regex("Destination: $destDir/(.*)")
            .findAll(processOutput)
            .map { it.groupValues[1] }
            .firstOrNull()
        if (fileName == null) {
            fileName = Regex(" $destDir/(.*) has already been downloaded")
                .findAll(processOutput)
                .map { it.groupValues[1] }
                .firstOrNull()
        }

        return fileName
    }

}