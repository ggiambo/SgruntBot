package com.fdtheroes.sgruntbot.actions.persistence;

import com.fdtheroes.sgruntbot.actions.models.Utonto
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDate
import java.time.Period

@Service
class UsersService(
    private val usersRepository: UtontiRepository,
) {

    fun getUser(userId: Long) = usersRepository.findByUserId(userId)

    fun getAllUsers(getChatMember: (Long) -> User?): List<User> {
        return usersRepository.findAll()
            .mapNotNull { getChatMember(it.userId!!) }
    }

    fun checkAndUpdate(user: User) {
        var utonto = usersRepository.findByUserId(user.id)
        if (utonto == null) {
            utonto = usersRepository.save(
                Utonto(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    userName = user.userName,
                    isBot = user.isBot,
                    userId = user.id
                )
            )
        }
        val delta = Period.between(utonto.updated, LocalDate.now())
        if (delta.days >= 3) {
            utonto.firstName = user.firstName
            utonto.firstName = user.firstName
            utonto.lastName = user.lastName
            utonto.userName = user.userName
            usersRepository.save(utonto)
        }
    }
}
