package com.fdtheroes.sgruntbot.actions.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

object KarmaRepository {

    private val dailyKarmaCredit = 5

    init {
        Database.connect(
            url = "jdbc:mariadb://127.0.0.1:3306/sgrunt",
            user = "sgrunt",
            password = "sgrunt",
        )
    }

    fun precheck(forUserId: Long) {
        transaction {
            val result = Karma.select { Karma.userId eq forUserId }.singleOrNull()
            if (result == null) {
                initKarmaData(forUserId)
                return@transaction
            }

            if (result[Karma.creditUpdated].isBefore(LocalDate.now())) {
                resetCreditForToday(forUserId)
                return@transaction
            }
        }
    }

    fun getKarma(userId: Long) = getColumn(userId, Karma.karma)

    fun getKarmaCredit(forUserId: Long) = getColumn(forUserId, Karma.karmaCredit)

    fun giveKarma(donatore: Long, ricevente: Long) {
        takeGiveKarma(ricevente, Int::inc)
        decCredit(donatore)
    }

    fun takeKarma(donatore: Long, ricevente: Long) {
        takeGiveKarma(ricevente, Int::dec)
        decCredit(donatore)
    }

    fun takeGiveKarma(ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val updatedKarma = newKarma(getKarma(ricevente))
        transaction {
            Karma.update({ Karma.userId eq ricevente }) {
                it[karma] = updatedKarma
            }
        }
    }

    fun decCredit(donatore: Long) {
        val updatedCredit = getKarmaCredit(donatore) - 1
        transaction {
            Karma.update({ Karma.userId eq donatore }) {
                it[karmaCredit] = updatedCredit
            }
        }
    }

    fun getKarmas(): List<Pair<Long, Int>> {
        return transaction {
            Karma.selectAll().map {
                Pair(it[Karma.userId], it[Karma.karma])
            }
        }
    }

    private fun initKarmaData(forUserId: Long) {
        transaction {
            Karma.insert {
                it[userId] = forUserId
                it[karma] = 0
                it[karmaCredit] = dailyKarmaCredit
                it[creditUpdated] = LocalDate.now()
            }
        }
    }

    private fun <T> getColumn(forUserId: Long, column: Column<T>): T {
        return transaction {
            val result = Karma.select { Karma.userId eq forUserId }.single()
            return@transaction result[column]
        }
    }

    private fun resetCreditForToday(forUserId: Long) {
        Karma.update({ Karma.userId eq forUserId }) {
            it[karmaCredit] = dailyKarmaCredit
            it[creditUpdated] = LocalDate.now()
        }
    }
}