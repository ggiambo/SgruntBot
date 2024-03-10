package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.ErrePiGiService
import com.fdtheroes.sgruntbot.persistence.UsersService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

//@Service
class Attacca(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val errePiGiService: ErrePiGiService,
    private val usersService: UsersService,
) : MessageHandler(botUtils, botConfig), HasHalp {

    override fun handle(message: Message) {
        if (message.text == "!attacca") {
            val testo: String
            if (message.replyToMessage == null) {
                testo = "E chi vorresti mai attaccare, grullo?"
            } else {
                testo = errePiGiService.attacca(message.from, message.replyToMessage.from)
            }
            botUtils.rispondi(ActionResponse.message(testo), message)
        }
        if (message.text == "?attacca") {
            val difensore = usersService
                .getAllUsers()
                .random()

            val testoAttacco = errePiGiService.attacca(message.from, difensore)
            val testo = "${botUtils.getUserName(message.from)} ubriaco fradicio tenta di attaccare ${
                botUtils.getUserName(difensore)
            }"
            botUtils.rispondi(ActionResponse.message("$testo\n$testoAttacco"), message)
        }
    }

    override fun halp() =
        "<b>!attacca</b> Rispondi a un messaggio con questo testo per attaccare l'autore\n<b>?attacca</b> Attacco random."

}
