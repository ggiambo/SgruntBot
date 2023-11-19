package com.fdtheroes.sgruntbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.boot.fromApplication
import org.springframework.boot.runApplication
import org.springframework.boot.system.ApplicationPid

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args) {
        this.addListeners(ApplicationPidFileWriter("sgruntbot.pid"))
    }
}
