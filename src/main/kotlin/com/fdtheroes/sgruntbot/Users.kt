package com.fdtheroes.sgruntbot

enum class Users(val id: Long) {
    SUORA(32657811),
    GIAMBO(353708759),
    DADA(252800958),
    SEU(68714652),
    GENGY(259607683),
    AVVE(10427888),
    LICHENE(5707999649),
    ALE(5770928065)
    ;

    companion object {
        private val userIds = values().associateBy { it.id }
        fun byId(id: Long) = userIds[id]
    }

}