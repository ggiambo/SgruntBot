package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.KarmaService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random.Default.nextInt

//@Service
class Cazzate(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val karmaService: KarmaService,
) : MessageHandler(botUtils, botConfig) {

    private val insulti = listOf("cazzate", "stronzate", "stupidate", "vaccate", "boiate figliuolo")
    private val complimenti = listOf(
        "Amen, AMEN!",
        "Questa è una grande verità",
        "WOW, non ci avevo mai pensato!",
        "Stai zitto e baciami, ora!",
        "Giusto, non avrei potuto dirlo meglio",
    )

    override fun handle(message: Message) {
        if (nextInt(300) == 0) {
            if (riceveComplimento(message.from.id)) {
                botUtils.rispondi(ActionResponse.message(complimenti.random()), message)
            } else {
                botUtils.rispondi(ActionResponse.message(insulta(message)), message)
            }
        }
    }

    // decide se complimentare o insultare a seconda del karma
    private fun riceveComplimento(userId: Long): Boolean {
        val karmas = karmaService.getKarmas().map { it.karma }
        val maxKarma = karmas.max()
        val minKarma = karmas.min()
        val userKarma = karmaService.getKarma(userId).karma
        return nextInt(minKarma, maxKarma) < userKarma
    }

    private fun insulta(message: Message): String {
        return if (nextInt(5) == 0) {
            val userName = botUtils.getUserName(botUtils.getChatMember(message.from.id))
            "Ma chiudi il becco, $userName!"
        } else {
            "Ma la smetti di dire ${insulti.random()}?"
        }
    }

}
