package com.fdtheroes.sgruntbot.actions.persistence;

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDate
import java.time.Period

@Service
class UsersService(
    private val usersRepository: UtontiRepository,
) {
    fun getAllUsers(getChatMember: (Long) -> User?): List<User> {
        return usersRepository.findAll()
            .map { it.userId }
            .mapNotNull { getChatMember(it) }
    }

    fun checkAndUpdate(user: User) {
        var utonto = usersRepository.findByUserId(user.id)
        if (utonto == null) {
            usersRepository.createUtonto(user.id, user.firstName, user.lastName, user.userName, user.isBot)
            utonto = usersRepository.getByUserId(user.id)
        }
        val delta = Period.between(utonto.updated, LocalDate.now())
        if (delta.days >= 3) {
            usersRepository.updateUtonto(utonto.userId, utonto.firstName, utonto.lastName, utonto.userName)
        }
    }
}
