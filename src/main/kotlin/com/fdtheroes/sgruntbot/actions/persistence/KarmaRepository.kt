package com.fdtheroes.sgruntbot.actions.persistence

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class KarmaRepository {

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
        val updatedCredit = getKarmaCredit(donatore) - 1
        val updatedKarma = getKarma(ricevente) + 1
        transaction {
            Karma.update({ Karma.userId eq donatore }) {
                it[karmaCredit] = updatedCredit
            }
            Karma.update({ Karma.userId eq ricevente }) {
                it[karma] = updatedKarma
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