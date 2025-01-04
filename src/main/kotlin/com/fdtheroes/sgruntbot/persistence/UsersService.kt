package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Utonto
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class UsersService(
    private val botUtils: BotUtils,
    private val usersRepository: UtontiRepository,
) {

    fun getUser(userId: Long) = usersRepository.findByUserId(userId)

    fun getAllUsers(): List<User> {
        return usersRepository.findAll()
            .mapNotNull { botUtils.getChatMember(it.userId!!) }
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
        val delta = ChronoUnit.DAYS.between(utonto.updated, LocalDate.now())
        if (delta >= 3) {
            utonto.firstName = user.firstName
            utonto.firstName = user.firstName
            utonto.lastName = user.lastName
            utonto.userName = user.userName
            usersRepository.save(utonto)
        }
    }
}
