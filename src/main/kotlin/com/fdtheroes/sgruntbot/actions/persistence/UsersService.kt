package com.fdtheroes.sgruntbot.actions.persistence;

import com.fdtheroes.sgruntbot.actions.models.Utonto
import jakarta.enterprise.context.ApplicationScoped
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDate
import java.time.Period

@ApplicationScoped
class UsersService(
    private val usersRepository: UtontiRepository,
) {

    fun getUser(userId: Long) = usersRepository.findById(userId)

    fun getAllUsers(getChatMember: (Long) -> User?): List<User> {
        return usersRepository.listAll()
            .mapNotNull { getChatMember(it.userId!!) }
    }

    fun checkAndUpdate(user: User) {
        var utonto = usersRepository.findById(user.id)
        if (utonto == null) {
            utonto = Utonto(
                firstName = user.firstName,
                lastName = user.lastName,
                userName = user.userName,
                isBot = user.isBot,
                userId = user.id
            )
            usersRepository.persist(utonto)
        }
        val delta = Period.between(utonto.updated, LocalDate.now())
        if (delta.days >= 3) {
            utonto.firstName = user.firstName
            utonto.firstName = user.firstName
            utonto.lastName = user.lastName
            utonto.userName = user.userName
            usersRepository.persist(utonto)
        }
    }
}
