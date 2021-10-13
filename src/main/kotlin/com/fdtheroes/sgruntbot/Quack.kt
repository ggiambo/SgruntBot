package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import org.reflections.Reflections

fun main() {
    Reflections("com.fdtheroes.sgruntbot")
        .getSubTypesOf(Action::class.java)
        //.forEach { println(it.name) }
        .forEach { it.getDeclaredConstructor().newInstance() }
}