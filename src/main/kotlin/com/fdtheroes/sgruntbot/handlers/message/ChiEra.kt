package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.UsersService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random.Default.nextInt

@Service
class ChiEra(
    private val usersService: UsersService,
    botUtils: BotUtils,
    botConfig: BotConfig,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex(
        "^!chiera$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text) && botConfig.lastSuper != null) {
            val user = if (nextInt(2) == 0) {
                usersService.getAllUsers().random()
            } else {
                botConfig.lastSuper
            }

            val messaggio = "${botUtils.getUserLink(user)} forse. Forse no. Boh? 🤷‍♂️"
            botUtils.rispondi(ActionResponse.message(messaggio), message)
        }
    }

    override fun halp() = "<b>!chiera</b> ti dice chi ha usato per ultimo il comando !super"
}
