package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.stereotype.Component
import java.lang.reflect.Method
import java.time.LocalDate

@Component("HoroscopeKeyGenerator")
class HoroscopeKeyGenerator : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        val segno = params.first()
        return "${LocalDate.now()}_$segno"
    }
}