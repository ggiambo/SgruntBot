package com.fdtheroes.sgruntbot.actions.persistence;

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User

@Service
class UsersService(
    private val statsRepository: StatsRepository,
) {
    fun getAllUsers(sgruntBot: SgruntBot): List<User> {
        return statsRepository.allIds()
            .mapNotNull { sgruntBot.getChatMember(it) }
    }
}
