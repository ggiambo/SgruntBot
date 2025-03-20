package com.fdtheroes.sgruntbot.handlers.message.oroscopo

class PlanetPosition(
    val longitude: Double,
    val sign: Sign,
    val longitudeIntoSign: Double,
    val planet: Planet,
    val retrograde: Boolean = false,
    val enteringSign: Sign? = null,
    val leavingSign: Sign? = null,
    val moonPhase: String? = null, // New field for moon phase
) {
    fun hasSign(sign: Sign): Boolean {
        return this.sign == sign || enteringSign == sign || leavingSign == sign
    }
}