package com.fdtheroes.sgruntbot.actions.persistence;

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User

@Service
class UsersService(
    private val statsRepository: StatsRepository,
) {
    fun getAllUsers(getChatMember: (Long) -> User?): List<User> {
        return statsRepository.allIds()
            .mapNotNull { getChatMember(it) }
    }
}
