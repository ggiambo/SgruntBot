package com.fdtheroes.sgruntbot

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import javax.annotation.PostConstruct

@Service
class SgruntBot(private val bot: Bot) : TelegramBotsApi(DefaultBotSession::class.java) {

    @PostConstruct
    fun init() {
        registerBot(bot)
    }
}
