package com.fdtheroes.sgruntbot.actions.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object Karma : Table("karma") {
    val userId = long("user_id")
    val karma = integer("karma")
    val karmaCredit = integer("karma_credit")
    val creditUpdated = date("credit_updated")

    override val primaryKey = PrimaryKey(userId)
}
