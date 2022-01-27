package com.fdtheroes.sgruntbot

import org.telegram.telegrambots.bots.DefaultBotOptions
import java.io.File
import java.net.URI

class BotConfig(args: Array<String>) {

    val botName = "SgruntBot"
    val token: String = File("token.txt").readText().trim()
    val imgurClientId = File("imgurClientId.txt").readText().trim()
    val defaultBotOptions = getDefaultBotOptions(args)

    private fun getDefaultBotOptions(args: Array<String>): DefaultBotOptions {
        if (args.isEmpty()) {
            return DefaultBotOptions()
        }

        var proxy = ""
        args.forEachIndexed { index, s ->
            if (s == "-proxy") {
                proxy = args[index + 1]
            }
        }
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
}