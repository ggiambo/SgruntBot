package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.springframework.stereotype.Service
import kotlin.random.Random.Default.nextBoolean

@Service
class ScheduledRandomKarma(
    private val usersService: UsersService,
    private val karmaService: KarmaService,
    private val botUtils: BotUtils,
    private val sgruntBot: Bot,
) : ScheduledRandom {

    override fun execute() {
        val vittima = usersService
            .getAllActiveUsers { sgruntBot.getChatMember(it) }
            .filter { it.id != Users.BLAHBANFBOT.id }    // filtra sgrunty
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

        sgruntBot.messaggio(ActionResponse.message(testo, false))
    }

}
