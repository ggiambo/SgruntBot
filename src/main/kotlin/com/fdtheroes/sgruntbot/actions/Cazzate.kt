package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import kotlin.random.Random.Default.nextInt

//@Service
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

    override fun doAction(ctx: ActionContext) {
        if (nextInt(150) == 0) {
            if (riceveComplimento(ctx.message.from.id)) {
                ctx.addResponse(ActionResponse.message(complimenti.random()))
            } else {
                ctx.addResponse(ActionResponse.message(insulta(ctx)))
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

    private fun insulta(ctx: ActionContext): String {
        return if (nextInt(5) == 0) {
            val userName = botUtils.getUserName(ctx.getChatMember(ctx.message.from.id))
            "Ma chiudi il becco, $userName!"
        } else {
            "Ma la smetti di dire ${insulti.random()}?"
        }
    }

}
