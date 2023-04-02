package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.random.Random.Default.nextInt

@Service
class Cazzate(
    private val botUtils: BotUtils,
    private val karmaService: KarmaService,
) : Action {

    private val insulti = listOf("cazzate", "stronzate", "stupidate", "boiate figliuolo")
    private val complimenti = listOf(
        "Amen, AMEN!",
        "Questa è una grande verità",
        "WOW, non ci avevo mai pensato!",
        "Stai zitto e baciami, ora!",
    )

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (nextInt(100) == 0) {
            if (riceveComplimento(message.from.id)) {
                complimenta(message, sgruntBot)
            } else {
                insulta(message, sgruntBot)
            }
        }
    }

    // decide se complimentare o insultare a seconda del karma
    private fun riceveComplimento(userId: Long): Boolean {
        val karmas = karmaService.getKarmas().map { it.second }
        val maxKarma = karmas.max()
        val minKarma = karmas.min()
        val userKarma = karmaService.getKarma(userId)
        return nextInt(minKarma, maxKarma) < userKarma
    }

    private fun complimenta(message: Message, sgruntBot: SgruntBot) {
        sgruntBot.rispondi(message, complimenti.random())
    }

    private fun insulta(message: Message, sgruntBot: SgruntBot) {
        if (nextInt(5) == 0) {
            val userName = botUtils.getUserName(sgruntBot.getChatMember(message.from.id))
            sgruntBot.rispondi(message, "Ma chiudi il becco, $userName!")
        } else {
            sgruntBot.rispondi(message, "Ma la smetti di dire ${insulti.random()}?")
        }
    }

}
