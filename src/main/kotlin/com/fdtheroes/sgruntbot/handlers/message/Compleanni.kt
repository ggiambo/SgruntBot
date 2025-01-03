package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class Compleanni(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {
    val dataCompleanno : DateTimeFormatter = DateTimeFormatter.ofPattern("dd LLLL", Locale.ITALIAN)
    private val regex = Regex("^!compleanni", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            val risposta = Users.compleanni.joinToString(separator = "\n") {
                val userName = botUtils.getUserName(botUtils.getChatMember(it.id))
                val data = dataCompleanno.format(it.compleanno!!.data())
                "\uD83C\uDF82 $userName: $data "
            }
            botUtils.rispondi(ActionResponse.message(risposta), message)
        }
    }

    override fun halp() =
        "<b>!compleanni</b> Tutti i cimpleanni conosciuti"
}