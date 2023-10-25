package com.fdtheroes.sgruntbot

import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.User
import java.net.URI
import java.time.LocalDateTime

@ApplicationScoped
class BotConfig(
    @ConfigProperty(name = "chatId") val chatId: String,
    @ConfigProperty(name = "telegramToken") val telegramToken: String,
    @ConfigProperty(name = "imgUrToken") val imgUrToken: String,
) {

    val botName = "SgruntBot"
    val defaultBotOptions = initDefaultBotOptions()

    var lastSuper: User? = null
    var lastAuthor: User? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null

    private fun initDefaultBotOptions(): DefaultBotOptions {
        val proxy = System.getenv()["https_proxy"]
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

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
    }

}
