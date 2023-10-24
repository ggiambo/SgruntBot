package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Karma
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class KarmaRepository : PanacheRepository<Karma> {

    fun getByUserId(userId: Long) = find("user_id", userId).firstResult()!!

    companion object {
        const val dailyKarmaCredit = 5
    }

}
