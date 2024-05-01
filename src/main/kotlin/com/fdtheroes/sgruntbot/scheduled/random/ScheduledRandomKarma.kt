package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.KarmaService
import com.fdtheroes.sgruntbot.persistence.UsersService
import com.fdtheroes.sgruntbot.utils.BotUtils
import kotlin.random.Random.Default.nextBoolean

//@Service
class ScheduledRandomKarma(
    private val usersService: UsersService,
    private val karmaService: KarmaService,
    private val botUtils: BotUtils,
) : ScheduledRandom {

    override fun execute() {
        val vittima = usersService
            .getAllUsers()
            .random()
        val giveKarma = nextBoolean()
        val azione = if (giveKarma) "aumentato" else "diminuito"
        if (giveKarma) {
            karmaService.takeGiveKarma(vittima.id) { it + 1 }
        } else {
            karmaService.takeGiveKarma(vittima.id) { it - 1 }
        }

        val testo =
            "${botUtils.getUserLink(vittima)} in verità in verità ti dico: Sgrunty da, Sgrunty toglie.\nIl tuo karma è $azione di 1."

        botUtils.messaggio(ActionResponse.message(testo))
    }

}
