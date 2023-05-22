package com.fdtheroes.sgruntbot

enum class Users(val id: Long) {
    SUORA(32657811L),
    GIAMBO(353708759L),
    DADA(252800958L),
    SEU(68714652L),
    GENGY(259607683L),
    AVVE(10427888L),
    ONCHAIN(6186884854L);

    companion object {
        private val userIds = values().associateBy { it.id }
        fun byId(id: Long) = userIds[id]
    }

}