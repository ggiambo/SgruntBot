package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.Users
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.random.Random.Default.nextInt

@Service
class Cazzate : Action {

    private val cazzate = listOf("cazzate", "stronzate", "stupidate", "boiate figliolo")
    private val rispondiGiambo = listOf(
        "Amen, AMEN!",
        "Questa è una grande verità",
        "WOW, non ci avevo mai pensato!"
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (nextInt(200) == 0) {
            sgruntBot.rispondi(message, getMessaggio(message.from))
        }
    }

    fun getMessaggio(from: User): String {
        if (from.id == Users.GIAMBO.id) {
            return rispondiGiambo.random()
        } else {
            return "Ma la smetti di dire ${cazzate.random()}?"
        }
    }
}
