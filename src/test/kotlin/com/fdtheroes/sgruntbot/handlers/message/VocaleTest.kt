package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BaseTest
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.ActionResponseType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.spy
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.io.ByteArrayInputStream

class VocaleTest : BaseTest() {

    private val vocale = spy(Vocale(botUtils, botConfig))

    init {
        doAnswer {
            InputFile(ByteArrayInputStream("contenutoMedia".encodeToByteArray()), "NomeDelVocale.mp3")
        }.whenever(vocale).getVocale(isA())
    }

    @Test
    fun handle_noReaction() {
        vocale.handle(message("Quack"))

        assertThat(actionResponses).hasSize(0)
    }

    @Test
    fun handle_empty() {
        vocale.handle(message("!vocale"))

        assertThat(actionResponses).hasSize(0)
    }

    @Test
    fun handle() {
        vocale.handle(message("!vocale dimmi qualcosa di interessante"))

        assertThat(actionResponses).hasSize(1)
        val actionResponse = actionResponses[0]
       assertThat(actionResponse.type).isEqualTo(ActionResponseType.Audio)
       assertThat(actionResponse.inputFile!!.mediaName).isEqualTo("NomeDelVocale.mp3")
       assertThat(actionResponse.inputFile!!.newMediaStream.readAllBytes().decodeToString()).isEqualTo("contenutoMedia")
       assertThat(actionResponse.inputFile!!.newMediaFile).isNull()
       assertThat(actionResponse.inputFile!!.attachName).isEqualTo("attach://NomeDelVocale.mp3")
       assertThat(actionResponse.inputFile!!.isNew).isTrue()
       assertThat(actionResponse.message).isEqualTo("LaVoceDiSgrunty.mp3")

        val argumentCaptor = argumentCaptor<ActionResponse, Message>()
        verify(botUtils, times(1)).rispondi(
            argumentCaptor.first.capture(),
            argumentCaptor.second.capture(),
        )
        val response = argumentCaptor.first.firstValue
        assertThat(response).isEqualTo(actionResponse)
        val message = argumentCaptor.second.firstValue
        assertThat(message).isNotNull
        assertThat(message.chatId).isEqualTo(-9999)
    }

    @Test
    fun handleSuper() {
        vocale.handle(message("!vocalesuper dimmi qualcosa di interessante, ma nell'altra chat"))

        assertThat(actionResponses).hasSize(1)
        val actionResponse = actionResponses[0]
        assertThat(actionResponse.type).isEqualTo(ActionResponseType.Audio)
        assertThat(actionResponse.inputFile!!.mediaName).isEqualTo("NomeDelVocale.mp3")
        assertThat(actionResponse.inputFile!!.newMediaStream.readAllBytes().decodeToString()).isEqualTo("contenutoMedia")
        assertThat(actionResponse.inputFile!!.newMediaFile).isNull()
        assertThat(actionResponse.inputFile!!.attachName).isEqualTo("attach://NomeDelVocale.mp3")
        assertThat(actionResponse.inputFile!!.isNew).isTrue()
        assertThat(actionResponse.message).isEqualTo("LaVoceDiSgrunty.mp3")

        val argumentCaptor = argumentCaptor<ActionResponse>()
        verify(botUtils, times(1)).messaggio(
            argumentCaptor.capture(),
        )
        val response = argumentCaptor.firstValue
        assertThat(response).isEqualTo(actionResponse)
    }
}