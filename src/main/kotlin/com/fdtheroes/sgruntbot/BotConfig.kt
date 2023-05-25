package com.fdtheroes.sgruntbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.User
import java.io.File
import java.net.URI
import java.time.LocalDateTime

@Service
class BotConfig(
    @Value("\${sgruntbot.config.chat-id}") val chatId: String,
    @Value("\${sgruntbot.config.telegram-token-file}") telegramTokenFile: String,
    @Value("\${sgruntbot.config.imgur-clientid-file}") imgUrClientIdFile: String,
    @Value("\${sgruntbot.config.proxy:}") proxy: String,
) {

    val botName = "SgruntBot"
    val token: String = File(telegramTokenFile).readText().trim()
    val clientId: String = File(imgUrClientIdFile).readText().trim()
    val defaultBotOptions = getDefaultBotOptions(proxy)

    var lastSuper: User? = null
    var lastAuthor: User? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null

    private fun getDefaultBotOptions(proxy: String): DefaultBotOptions {
        if (proxy.isEmpty()) {
            return DefaultBotOptions()
        }
        val uri = URI(proxy)
        return DefaultBotOptions().apply {
            this.proxyType = DefaultBotOptions.ProxyType.HTTP
            this.proxyHost = uri.host
            this.proxyPort = uri.port
        }
    }

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
    }

}
