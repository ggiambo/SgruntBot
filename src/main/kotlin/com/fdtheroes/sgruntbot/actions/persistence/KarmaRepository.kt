package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface KarmaRepository : CrudRepository<Karma, Long> {

    @Modifying
    @Query("update sgrunt.karma set sgrunt.karma.karma_credit = :updatedCredit where user_id = :userId")
    fun updateCredit(updatedCredit: Int, userId: Long)

    @Query("select karma_credit from sgrunt.karma where user_id = :userId")
    fun getKarmaCredit(userId: Long): Int

    @Query("select karma from sgrunt.karma where user_id = :userId")
    fun getKarma(userId: Long): Int

    @Modifying
    @Query("update sgrunt.karma set sgrunt.karma.karma = :updatedKarma where user_id = :userId")
    fun updateKarma(updatedKarma: Int, userId: Long)

    @Modifying
    @Query("update sgrunt.karma set sgrunt.karma.karma_credit = ${dailyKarmaCredit}, sgrunt.karma.credit_updated = :creditUpdated where user_id = :userId")
    fun resetCreditForToday(userId: Long, creditUpdated: LocalDateTime = LocalDateTime.now())

    companion object {
        const val dailyKarmaCredit = 5
    }

}