package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.UsersService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Utonti(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val usersService: UsersService,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!utonti$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.matches(message.text)) {
            val utonti = usersService.getAllUsers()
                .joinToString(separator = "\n") { "- <code>${it.id}</code>: ${botUtils.getUserLink(it)}" }
            botUtils.rispondi(ActionResponse.message("Utonti di questa ciat:\n${utonti}"), message)
        }
        usersService.checkAndUpdate(message.from)
        if (message.replyToMessage?.from != null) {
            usersService.checkAndUpdate(message.replyToMessage.from)
        }
    }

    override fun halp() = "<b>!utonti</b> lista degli IDs degli utonti che hanno partecipato a questa ciat"
}
