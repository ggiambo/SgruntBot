package com.fdtheroes.sgruntbot.handlers.message.oroscopo

enum class Sign(
    val index: Int,
    val nome: String,
    val articolo: String,
    val nel: String,
    val dal: String,
) {
    ARIETE(0, "Ariete", "l'", "nell'", "dall'"),
    TORO(1, "Toro", "il", "nel", "dal"),
    GEMELLI(2, "Gemelli", "i", "nei", "dai"),
    CANCRO(3, "Cancro", "il", "nel", "dal"),
    LEONE(4, "Leone", "il", "nel", "dal"),
    VERGINE(5, "Vergine", "la", "nella", "dalla"),
    BILANCIA(6, "Bilancia", "la", "nella", "dalla"),
    SCORPIONE(7, "Scorpione", "lo", "nello", "dallo"),
    SAGGITARIO(8, "Sagittario", "il", "nel", "dal"),
    CAPRICORNO(9, "Capricorno", "il", "nel", "dal"),
    ACQUARIO(10, "Acquario", "l'", "nell'", "dall'"),
    PESCI(11, "Pesci", "i", "nei", "dai"),
    ;

    fun getSignNameWithPreposition(preposition: String) : String {
        val p = when (preposition) {
            "" -> articolo
            "in" -> nel
            "da" -> dal
            else -> ""
        }

        val adjustedP = if (!p.endsWith("'")) "$p " else p

        return "$adjustedP$nome"
    }

    companion object {
        private val byIndex = entries.associateBy { it.index }
        fun byIndex(index: Int) = byIndex[index]

        private val byNome = entries.associateBy { it.nome.lowercase() }
        fun byNome(nome: String) = byNome[nome.lowercase()]
    }
}