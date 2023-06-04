package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Utonti(
    private val botUtils: BotUtils,
    private val botConfig: BotConfig,
    private val usersService: UsersService,
) : Action, HasHalp {

    private val regex = Regex("^!utonti$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (regex.matches(message.text)) {
            val utonti = usersService.getAllUsers(doNext)
                .joinToString(separator = "\n") { "- ${it.id}: ${botUtils.getUserName(it)}" }
            doNext(ActionResponse.message("Utonti di questa ciat:\n${utonti}"))
        }
    }

    override fun halp() = "<b>!utonti</b> lista degli IDs degli utonti che hanno partecipato a questa ciat"
}
