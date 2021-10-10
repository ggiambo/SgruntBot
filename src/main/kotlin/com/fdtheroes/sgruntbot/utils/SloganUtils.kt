package com.fdtheroes.sgruntbot.utils

import java.net.URL

class SloganUtils {

    fun slogan(name: String?): String {
        if (name == null) {
            return ""
        }

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

        return out
    }
}