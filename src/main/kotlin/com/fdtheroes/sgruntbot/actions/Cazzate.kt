package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.Users
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

@Service
class Cazzate(private val botUtils: BotUtils) : Action {

    private val cazzate = listOf("cazzate", "stronzate", "stupidate", "boiate figliolo")
    private val rispondiGiambo = listOf(
        "Amen, AMEN!",
        "Questa è una grande verità",
        "WOW, non ci avevo mai pensato!",
        "Giambo, tu si che sai parlare bene!",
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (nextInt(200) == 0) {
            val fromId = message.from.id
            if (fromId == Users.GIAMBO.id) {
                sgruntBot.rispondi(message, rispondiGiambo.random())
            } else {
                if (nextInt(5) == 0) {
                    val userName = botUtils.getUserName(sgruntBot.getChatMember(fromId))
                    sgruntBot.rispondi(message, "Ma chiudi il becco, $userName!")
                } else {
                    sgruntBot.rispondi(message, "Ma la smetti di dire ${cazzate.random()}?")
                }
            }
        }
    }

}
