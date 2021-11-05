package com.fdtheroes.sgruntbot.actions.persistence

import java.sql.DriverManager
import java.sql.Timestamp
import java.time.LocalDate

class KarmaRepository {

    private val connection = DriverManager.getConnection(
        "jdbc:mariadb://127.0.0.1:3306/sgrunt",
        "sgrunt",
        "sgrunt"
    ).apply { autoCommit = true }

    fun precheck(userId: Long) {
        with(connection.prepareStatement(select)) {
            setLong(1, userId)
            with(executeQuery()) {
                if (!next()) {
                    initKarmaData(userId)
                } else {
                    val creditUpdated = getTimestamp(2).toLocalDateTime()
                    if (creditUpdated.isBefore(LocalDate.now().atStartOfDay())) {
                        resetCreditForToday(userId)
                    }
                }
            }
        }
    }

    fun getKarma(userId: Long): Int {
        with(connection.prepareStatement(getKarma)) {
            setLong(1, userId)

            with(executeQuery()) {
                next()
                return getInt(1)
            }
        }
    }

    fun getKarmaCredit(userId: Long): Int {
        with(connection.prepareStatement(getKarmaCredit)) {
            setLong(1, userId)

            with(executeQuery()) {
                next()
                return getInt(1)
            }
        }
    }

    fun giveKarma(donatore: Long, ricevente: Long) {
        takeGiveKarma(donatore, ricevente, Int::inc)
    }

    fun takeKarma(donatore: Long, ricevente: Long) {
        takeGiveKarma(donatore, ricevente, Int::dec)
    }

    fun takeGiveKarma(donatore: Long, ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val updatedCredit = getKarmaCredit(donatore) - 1
        val updatedKarma = newKarma(getKarma(ricevente))

        with(connection.prepareStatement(updateKarmaCredit)) {
            setInt(1, updatedCredit)
            setLong(2, donatore)

            executeUpdate()
        }


        with(connection.prepareStatement(updateKarma)) {
            setInt(1, updatedKarma)
            setLong(2, ricevente)

            executeUpdate()
        }
    }

    private fun initKarmaData(userId: Long) {
        with(connection.prepareStatement(init)) {
            setLong(1, userId)
            setInt(2, 0)
            setInt(3, dailyKarmaCredit)
            setTimestamp(4, Timestamp.valueOf(LocalDate.now().atStartOfDay()))

            executeUpdate()
        }
    }

    private fun resetCreditForToday(userId: Long) {
        with(connection.prepareStatement(resetCredit)) {
            setLong(1, userId)
            setTimestamp(2, Timestamp.valueOf(LocalDate.now().atStartOfDay()))

            executeUpdate()
        }
    }

    companion object {

        private val dailyKarmaCredit = 5

        private val init = """
            INSERT INTO karma(user_id, karma, karma_credit, credit_updated) 
            VALUES (?, ?, ?, ?)
        """.trimIndent()

        private val select = """
            SELECT user_id, credit_updated 
            FROM karma 
            WHERE user_id = ?
        """.trimIndent()

        private val resetCredit = """
            UPDATE karma 
            SET karma_credit = ?, credit_updated = ?
        """.trimIndent()

        private val updateKarmaCredit = """
            UPDATE karma 
            SET karma_credit = ? 
            WHERE user_id = ?
        """.trimIndent()

        private val updateKarma = """
            UPDATE karma 
            SET karma = ? 
            WHERE user_id = ?
        """.trimIndent()

        private val getKarma = """
            SELECT karma
            FROM karma 
            WHERE user_id = ?
        """.trimIndent()

        private val getKarmaCredit = """
            SELECT karma_credit
            FROM karma 
            WHERE user_id = ?
        """.trimIndent()
    }

}