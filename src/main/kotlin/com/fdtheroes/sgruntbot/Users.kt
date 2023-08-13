package com.fdtheroes.sgruntbot

enum class Users(val id: Long) {
    DANIELE(32657811),
    BLAHBANFBOT(2097709389),
    GGIAMBO(353708759),
    DA_DA212(252800958),
    SHDX_T(68714652),
    F(259607683),
    IL_VINCI(104278889),
    LICHENE(5707999649),
    ALE(5770928065),
    ROBOBOT(6383648928),
    ;

    companion object {
        private val userIds = entries.associateBy { it.id }
        fun byId(id: Long) = userIds[id]
    }

}
