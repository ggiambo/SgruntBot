package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.utils.Disabled
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@EnableCaching
@ComponentScan(
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = [Disabled::class]
        )
    ]
)
@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
