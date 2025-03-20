package com.fdtheroes.sgruntbot.handlers.message.oroscopo

class HoroscopeParams(
    val sign: Sign,
    val planets: List<PlanetPosition>,
    val conjunctions: List<List<PlanetPosition>>,
    val oppositions: List<List<PlanetPosition>>,
)