package com.fdtheroes.sgruntbot

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Configuration
class BeanConfigurations {

    @Bean
    @ConditionalOnMissingBean(BuildProperties::class)
    fun buildProperties(): BuildProperties {
        val properties = Properties().apply {
            this["name"] = "Sgruntbot"
            this["version"] = "SNAPSHOT"
            this["time"] = DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now())
        }
        return BuildProperties(properties)
    }
}