package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BotUtilsTest : BaseTest() {

    @Test
    fun isMessageInChat() {
        assertThat(botUtils.isMessageInChat(message("dummy"))).isTrue()
    }

    @Test
    fun getUserName() {
        val userName = botUtils.getUserName(user())
        assertThat(userName).isEqualTo("Pippo")
    }

    @Test
    fun getUserLink() {
        val userLink = botUtils.getUserLink(user())
        assertThat(userLink).isEqualTo("<a href=\"tg://user?id=42\">Pippo</a>")
    }

    @Test
    fun rispondi() {
        botUtils.rispondi(ActionResponse.message("Pong"), message("Ping"))
        assertThat(actionResponses).hasSize(1)
        val actionResponse = actionResponses.first()
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).isEqualTo("Pong")
        assertThat(actionResponse.inputFile).isNull()
    }

    @Test
    fun messaggio() {
        botUtils.messaggio(ActionResponse.message("Prot"))
        assertThat(actionResponses).hasSize(1)
        val actionResponse = actionResponses.first()
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Message)
        assertThat(actionResponse.message).isEqualTo("Prot")
        assertThat(actionResponse.inputFile).isNull()
    }

    @Test
    fun getChatMember() {
        val chatMember = botUtils.getChatMember(99)!!
        assertThat(chatMember.id).isEqualTo(99)
        assertThat(chatMember.userName).isEqualTo("Username_99")
        assertThat(chatMember.firstName).isEmpty()
        assertThat(chatMember.lastName).isNull()
    }

    @Test
    fun trimString_full() {
        val trimmed = botUtils.trimString("1234567890", 11)
        assertThat(trimmed).isEqualTo("1234567890")
    }

    @Test
    fun trimString_one() {
        val trimmed = botUtils.trimString("1234567890", 1)
        assertThat(trimmed).isEqualTo("...")
    }

    @Test
    fun trimString() {
        val trimmed = botUtils.trimString("1234567890", 8)
        assertThat(trimmed).isEqualTo("12345...")
    }
}