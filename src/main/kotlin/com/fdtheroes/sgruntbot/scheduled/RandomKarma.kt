package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class RandomKarma(
    private val usersService: UsersService,
    private val karmaService: KarmaService,
    private val botUtils: BotUtils,
    sgruntBot: Bot,
    botConfig: BotConfig,
) : RandomScheduledAction(sgruntBot, botConfig) {

    override fun getMessageText(): String {
        val vittima = usersService
            .getAllUsers { sgruntBot.getChatMember(it) }
            .filter { it.id != Users.BLAHBANFBOT.id }    // e filtra sgrunty
            .filter { botUtils.getUserName(it).isNotEmpty() } // filtra gli inattivi
            .random()
        val giveKarma = Random.nextBoolean()
        val azione = if (giveKarma) "aumentato" else "diminuito"
        if (giveKarma) {
            karmaService.takeGiveKarma(vittima.id) { it + 1 }
        } else {
            karmaService.takeGiveKarma(vittima.id) { it - 1 }
        }
        return "${botUtils.getUserLink(vittima)} in verità in verità ti dico: Sgrunty da, Sgrunty toglie.\nIl tuo kama è $azione di 1."
    }

}
