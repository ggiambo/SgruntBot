package com.fdtheroes.sgruntbot

import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

fun main(args: Array<String>) {
    val config = BotConfig(args)
    val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
    botsApi.registerBot(Bot(config))
}