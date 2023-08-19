package com.fdtheroes.sgruntbot.actions.persistence;

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import kotlin.time.toKotlinDuration

@Service
class UsersService(
    private val usersRepository: UtontiRepository,
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

    fun checkAndUpdate(user: User) {
        var utonto = usersRepository.findByUserId(user.id)
        if (utonto == null) {
            usersRepository.createUtonto(user.id, user.firstName, user.lastName, user.userName)
            utonto = usersRepository.getByUserId(user.id)
        }
        val delta = Period.between(utonto.updated, LocalDate.now())
        if (delta.days >= 3) {
            usersRepository.updateUtonto(utonto.userId, utonto.firstName, utonto.lastName, utonto.userName)
        }
    }
}
