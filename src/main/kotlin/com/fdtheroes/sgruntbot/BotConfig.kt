package com.fdtheroes.sgruntbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.updates.AllowedUpdates
import org.telegram.telegrambots.meta.api.objects.User
import java.io.File
import java.net.URI
import java.time.LocalDateTime

@Service
class BotConfig(
    @Value("\${sgruntbot.config.chat-id}") val chatId: String,
    @Value("\${sgruntbot.config.telegram-token-file}") telegramTokenFile: String,
    @Value("\${sgruntbot.config.imgur-clientid-file}") imgUrClientIdFile: String,
) {

    val botName = "SgruntBot"
    val token: String = File(telegramTokenFile).readText().trim()
    val clientId: String = File(imgUrClientIdFile).readText().trim()
    val defaultBotOptions = initDefaultBotOptions()

    var lastSuper: User? = null
    var lastAuthor: User? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null

    private fun initDefaultBotOptions(): DefaultBotOptions {
        val defaultBotOptions = DefaultBotOptions()
        defaultBotOptions.allowedUpdates = listOf(
            AllowedUpdates.MESSAGE,
            "message_reaction",
        )

        val proxy = System.getenv()["https_proxy"]
        if (proxy.isNullOrEmpty()) {
            return defaultBotOptions
        }
        val uri = URI(proxy)
        defaultBotOptions.proxyType = DefaultBotOptions.ProxyType.HTTP
        defaultBotOptions.proxyHost = uri.host
        defaultBotOptions.proxyPort = uri.port

        return defaultBotOptions
    }

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
    }

}
