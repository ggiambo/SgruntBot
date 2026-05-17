package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OriginTest : BaseTest() {

    private val origin = Origin(botUtils, botConfig)

    @Test
    fun handle_origin() {
        origin.handle(message("!origin"))

        assertThat(actionResponses).hasSize(1)
        val response = actionResponses.first()
        assertThat(response.message).isEmpty()
        val content = response.inputFile!!.newMediaStream.readAllBytes()
        assertThat(content).hasSize(13068)
        assertThat(content.decodeToString()).startsWith("require 'telegram/bot'")
    }

    @Test
    fun handle_origini() {
        origin.handle(message("!origini"))

        assertThat(actionResponses).hasSize(1)
        val response = actionResponses.first()
        assertThat(response.message).isEmpty()
        val content = response.inputFile!!.newMediaStream.readAllBytes()
        assertThat(content).hasSize(13068)
        assertThat(content.decodeToString()).startsWith("require 'telegram/bot'")
    }

    @Test
    fun handle_originiiiii() {
        origin.handle(message("!originiiiii"))

        assertThat(actionResponses).hasSize(0)
    }
}