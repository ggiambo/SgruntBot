package com.fdtheroes.sgruntbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import java.io.File
import java.net.URI

@Service
class BotConfig(
    @Value("\${sgruntbot.config.telegram-token-file}") telegramTokenFile: String,
    @Value("\${sgruntbot.config.imgur-client-id-file}") imgurClientIdFile: String,
    @Value("\${sgruntbot.config.proxy}") proxy: String?,
) {

    val botName = "SgruntBot"
    val token: String = File(telegramTokenFile).readText().trim()
    val imgurClientId = File(imgurClientIdFile).readText().trim()
    val defaultBotOptions = getDefaultBotOptions(proxy)

    private fun getDefaultBotOptions(proxy: String?): DefaultBotOptions {
        if (proxy.isNullOrEmpty()) {
            return DefaultBotOptions()
        }

        val uri = URI(proxy)
        return DefaultBotOptions().apply {
            this.proxyType = DefaultBotOptions.ProxyType.HTTP
            this.proxyHost = uri.host
            this.proxyPort = uri.port
        }

    }
}
