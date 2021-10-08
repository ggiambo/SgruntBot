package com.fdtheroes.sgruntbot.utils

import java.net.URL

class SloganUtils {

    fun slogan(name: String?): String {
        if (name == null) {
            return ""
        }
        println("slogan")
        println(name)

        val res = URL("http://www.sloganizer.net/en/outbound.php?slogan=${name}")
            .openConnection()
            .getInputStream()
            .readAllBytes()
            .decodeToString()
        val out = Regex("<a.*?>(.*)</a>")
            .findAll(res)
            .map { it.groupValues[1] }
            .firstOrNull()
            .orEmpty()

        println("response = $out")
        return out
    }
}