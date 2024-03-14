package com.fdtheroes.sgruntbot.handlers.message

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class Rest(
    @Value("\${sgruntbot.config.baseRestUrl}") private val baseRestUrl: String,
    @Value("\${springdoc.swagger-ui.path}") private val swaggerUrl: String,
) : HasHalp {

    override fun halp() = "<b>${baseRestUrl}${swaggerUrl}</b> per una descrizione dei metodi dell'interfaccia REST"
}