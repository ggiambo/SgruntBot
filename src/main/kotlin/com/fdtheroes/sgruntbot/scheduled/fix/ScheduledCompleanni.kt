package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class ScheduledCompleanni(private val botUtils: BotUtils) : Scheduled {

    override fun firstRun() = getProssimoCompleanno()

    override fun nextRun() = getProssimoCompleanno()

    override fun execute() {
        val prossimoCompleannoUtente = getProssimoCompleannoUtente()
        val utenteId = prossimoCompleannoUtente.id
        val user = botUtils.getChatMember(utenteId)
        val userLink = botUtils.getUserLink(user)
        botUtils.messaggio(ActionResponse.message("\uD83C\uDF82 Tanti auguri $userLink \uD83C\uDF89 !"))
    }

    private fun getProssimoCompleannoUtente(): Users {
        val now = LocalDate.now()
        val prossimoCompleannoUtente = Users.compleanni.firstOrNull {
            it.compleanno!!.data().isAfter(now)
        }
        if (prossimoCompleannoUtente != null) {
            return prossimoCompleannoUtente
        }

        return Users.compleanni.first()
    }

    private fun getProssimoCompleanno(): LocalDateTime {
        val prossimoCompleanno = getProssimoCompleannoUtente().compleanno!!.data().atStartOfDay()
        if (prossimoCompleanno.isAfter(LocalDateTime.now())) {
            return prossimoCompleanno
        }

        return prossimoCompleanno.plusYears(1)
    }

}