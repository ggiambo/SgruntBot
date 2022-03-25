package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.pathString

@Service
class Canzone : Action, HasHalp {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val regex = Regex("!canzone (.*)$", RegexOption.IGNORE_CASE)
    private val destPath: Path

    init {
        val tmpDir = System.getProperty("java.io.tmpdir")
        destPath = Path(tmpDir, "songs")
        if (!destPath.exists()) {
            destPath.createDirectory()
        }
    }

    override fun doAction(message: Message) {
        val canzone = regex.find(message.text)?.groupValues?.get(1)
        if (canzone != null) {
            BotUtils.rispondi(SendChatAction(message.chat.id.toString(), ActionType.UPLOADDOCUMENT.toString()))

            val fileName = fetch(canzone)
            if (fileName == null) {
                BotUtils.rispondi(message, "Non ci riesco.")
                return
            }
            val file = destPath.resolve(fileName).toFile()
            if (file.exists()) {
                log.info("canzone da ${getSize(file)}")
            }

            val sendAudio = SendAudio()
            sendAudio.chatId = message.chat.id.toString()
            sendAudio.replyToMessageId = message.messageId
            sendAudio.audio = InputFile(file, fileName)

            BotUtils.rispondi(sendAudio).thenApply { file.delete() }
        }
    }

    override fun halp() = "<b>!canzone</b> <i>la tua canzone</i> cerca e scarica la tua canzone"

    private fun fetch(query: String): String? {
        val destDir = destPath.pathString
        val processOutput = ProcessBuilder()
            .command(
                "sh", "-c",
                """yt-dlp --restrict-filenames --extract-audio --audio-format mp3 --output "$destDir/%(title)s.mp3" "ytsearch1:$query" --geo-bypass-country IT 2>&1"""
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

    private fun getSize(file: File): String {
        val sizeInBytes = file.length()
        if (sizeInBytes < 1024) {
            return "$sizeInBytes bytes"
        }
        val sizeInKilo = sizeInBytes / 1024
        if (sizeInKilo < 1024) {
            return "$sizeInKilo K"
        }
        val sizeInMega = sizeInKilo / 1024
        if (sizeInMega < 1024) {
            return "$sizeInMega M"
        }
        val sizeInGiga = sizeInMega / 1024
        if (sizeInGiga < 1024) {
            return "$sizeInGiga G"
        }

        return "no, aspè, oltre 1 tera ?!?!"
    }

}
