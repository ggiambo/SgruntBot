package com.fdtheroes.sgruntbot.actions.persistence;

import com.fdtheroes.sgruntbot.Bot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User

@Service
class UsersService(
    private val statsRepository: StatsRepository,
    private val sgruntBot: Bot,
) {
    fun getAllUsers(): List<User> {
        return statsRepository.allIds()
            .mapNotNull { sgruntBot.getChatMember(it) }
    }
}
