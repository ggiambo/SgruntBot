package com.fdtheroes.sgruntbot.actions.persistence;

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User

@Service
class UsersService(
    private val statsRepository: StatsRepository,
    private val botUtils: BotUtils,
) {
    fun getAllUsers(getChatMember: (Long) -> User?): List<User> {
        return statsRepository.allIds()
            .mapNotNull { getChatMember(it) }
    }

    fun getAllActiveUsers(getChatMember: (Long) -> User?): List<User> {
        return getAllUsers(getChatMember)
            .filter { botUtils.getUserName(it).isNotEmpty() } // filtra gli inattivi
    }
}
