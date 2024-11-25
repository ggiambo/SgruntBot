package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ScheduledCompleanni(private val botUtils: BotUtils) : ScheduledAMezzanotte {

    override fun execute() {
        Users.compleanni
            .filter { it.compleanno?.data() == LocalDate.now() }
            .map { botUtils.getChatMember(it.id) }
            .map { botUtils.getUserLink(it) }
            .map { "\uD83C\uDF82 Tanti auguri $it \uD83C\uDF89 !" }
            .forEach { botUtils.messaggio(ActionResponse.message(it)) }
    }
}