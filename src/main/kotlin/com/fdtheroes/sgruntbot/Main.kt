package com.fdtheroes.sgruntbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
