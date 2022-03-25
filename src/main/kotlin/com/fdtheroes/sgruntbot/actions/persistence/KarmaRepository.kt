package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BotUtils
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Repository
interface KarmaRepository : CrudRepository<Karma, Long> {

    @Transactional
    fun precheck(forUserId: Long) {
        val result = findByIdOrNull(forUserId)
        if (result == null) {
            initKarmaData(forUserId)
            return
        }

        if (result.creditUpdated.isBefore(LocalDate.now())) {
            resetCreditForToday(forUserId = forUserId)
        }
    }

    fun getKarma(forUserId: Long) = findById(forUserId).get().karmaCredit

    fun getKarmaCredit(forUserId: Long) = findById(forUserId).get().karmaCredit

    // donatore da' del karma a ricevente, credito di donatore diminiuisce
    @Transactional
    fun takeGiveKarma(donatore: Long, ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        takeGiveKarma(ricevente, newKarma)
        val updatedCredit = getKarmaCredit(donatore) - 1
        updateCredit(updatedCredit, donatore)
    }

    // ricevente riceve del karma
    @Transactional
    fun takeGiveKarma(ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val updatedKarma = newKarma(findById(ricevente).get().karma)
        updateKarma(updatedKarma, ricevente)
    }

    @Modifying
    fun updateKarma(updatedKarma: Int, userId: Long) {
        val karma = findById(userId).get()
        karma.karma = updatedKarma
        save(karma)
    }

    @Modifying
    fun updateCredit(updatedCredit: Int, userId: Long) {
        val karma = findById(userId).get()
        karma.karmaCredit = updatedCredit
        save(karma)
    }

    fun getKarmas(): List<Pair<Long, Int>> {
        return findAll().map {
            Pair(it.userId, it.karma)
        }
    }

    @Modifying
    private fun initKarmaData(forUserId: Long) {
        val karma = Karma(
            userId = forUserId,
            karma = 0,
            karmaCredit = dailyKarmaCredit,
            creditUpdated = LocalDate.now(),
        )
        save(karma)
    }

    @Modifying
    @Query("update Karma k set k.karma_credit = :updatedCredit, k.credit_updated = :creditUpdated where k.user_id :userId")
    fun resetCreditForToday(
        updatedCredit : Int = dailyKarmaCredit,
        creditUpdated : LocalDate = LocalDate.now(),
        forUserId: Long
    )

    fun testoKarmaReport(): String {
        val karmas = getKarmas()
            .sortedByDescending { it.second }
            .map { "${getUserName(it.first).padEnd(20)}%3d".format(it.second) }
            .joinToString("\n")
        return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
    }

    private fun getUserName(userId: Long) = BotUtils.getUserName(BotUtils.getChatMember(userId))

    companion object {
        const val dailyKarmaCredit = 5
    }

}
