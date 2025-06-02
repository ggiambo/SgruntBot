package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists
import kotlin.io.path.pathString

@Service
class CanzoneYtDlp(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val regex = Regex("^!canzone (.*)$", RegexOption.IGNORE_CASE)
    private val destPath by lazy { initDestPath() }
    private val suoraProxy = "http://198.98.49.55:8118"

    private fun initDestPath(): Path {
        val tmpDir = System.getProperty("java.io.tmpdir")
        val path = Path(tmpDir, "songs")
        if (!path.exists()) {
            path.createDirectory()
        }
        return path
    }

    override fun handle(message: Message) {
        val canzone = regex.find(message.text)?.groupValues?.get(1)
        if (canzone != null) {
            val fileName = fetch(canzone)
            if (fileName == null) {
                botUtils.rispondi(ActionResponse.message("Non ci riesco."), message)
                return
            }
            val file = destPath.resolve(fileName).toFile()
            if (file.exists()) {
                log.info("canzone da ${getSize(file)}")
            }
            val audio = InputFile(file, fileName)
            botUtils.rispondi(ActionResponse.audio(fileName, audio), message)
        }
    }

    override fun halp() = "<b>!canzone</b> <i>la tua canzone</i> cerca e scarica la tua canzone"

    private fun fetch(query: String): String? {
        val destDir = destPath.pathString
        val command =
            """yt-dlp --restrict-filenames --proxy $suoraProxy --extract-audio --audio-format mp3 --output "$destDir/%(title)s.mp3" "ytsearch1:$query" --geo-bypass-country IT 2>&1"""
        log.info(command)
        val processOutput = ProcessBuilder()
            .command("sh", "-c", command)
            .start()
            .inputStream
            .bufferedReader()
            .readText()
        log.info(processOutput)

        return extractFilename(processOutput)
    }

    private fun extractFilename(processOutput: String): String? {
        val destDir = destPath.pathString
        var fileName = Regex("Destination: $destDir/(.+\\.mp3)")
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
