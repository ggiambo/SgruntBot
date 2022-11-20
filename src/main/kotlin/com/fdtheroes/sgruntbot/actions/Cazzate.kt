package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.persistence.ComplimentoService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

@Service
class Cazzate(private val complimentoService: ComplimentoService) : Action {

    private val cazzate = listOf(
        "cazzate",
        "stronzate",
        "stupidate",
        "boiate figliolo",
        "cose che direbbe solo Dada",
        "fake news",
    )
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
                    val complimento = complimentoService.get(message.from.id)
                    if (!complimento.isNullOrEmpty()) {
                        sgruntBot.rispondi(message, complimento)
                    }
                } else {
                    sgruntBot.rispondi(message, "Ma la smetti di dire ${cazzate.random()}?")
                }
            }
        }
    }

}
