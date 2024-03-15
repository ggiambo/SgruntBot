package com.fdtheroes.sgruntbot

enum class Users(val id: Long) {
    SPDT(24492275),
    DANIELE(32657811),
    SHDX_T(68714652),
    IL_VINCI(104278889),
    ALLEGRASOLITUDINE(250965179),
    DA_DA_212(252800958),
    F(259607683),
    GGIAMBO(353708759),
    BLAH_BANF_BOT(2097709389),
    ALE(5770928065),
    GENGIVA_BOT(6383648928),
    DADUNKEN_BOT(6470939064)
    ;

    companion object {
        private val userIds = entries.associateBy { it.id }
        fun byId(id: Long) = userIds[id]
    }

}
