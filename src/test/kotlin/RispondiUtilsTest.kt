import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.utils.RispondiUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message

class RispondiUtilsTest {

    private val rispondiUtils = RispondiUtils()

    @Test
    fun testRispondiAsText() {
        val bot = Mockito.mock(Bot::class.java)
        val captor = ArgumentCaptor.forClass(SendMessage::class.java)

        rispondiUtils.rispondiAsText(bot, message(), "risposta")

        Mockito.verify(bot, Mockito.times(1)).executeAsync(captor.capture())
        val risposta = captor.value
        assertThat(risposta.chatId).isEqualTo("99")
        assertThat(risposta.text).isEqualTo("risposta")
        assertThat(risposta.replyToMessageId).isEqualTo(42)
        assertThat(risposta.parseMode).isNull()
        assertThat(risposta.disableWebPagePreview ?: null).isNull() // elvis needed because of auto unboxing
        assertThat(risposta.disableNotification ?: null).isNull()
        assertThat(risposta.replyMarkup).isNull()
        assertThat(risposta.entities).isNull()
        assertThat(risposta.allowSendingWithoutReply ?: null).isNull()
    }

    @Test
    fun testRispondi_typing() {
        val bot = Mockito.mock(Bot::class.java)
        val captor = ArgumentCaptor.forClass(SendChatAction::class.java)

        rispondiUtils.rispondi(bot, message(), "risposta")

        Mockito.verify(bot, Mockito.times(2)).executeAsync(captor?.capture())

        val typing = captor.allValues[0]
        assertThat(typing.chatId).isEqualTo("99")
        assertThat(typing.action).isEqualTo(ActionType.TYPING.toString())
        assertThat(typing.actionType).isEqualTo(ActionType.TYPING)
        assertThat(typing.method).isEqualTo("sendChatAction")
    }

    @Test
    fun testRispondi_message() {
        val bot = Mockito.mock(Bot::class.java)
        val captor = ArgumentCaptor.forClass(SendMessage::class.java)

        rispondiUtils.rispondi(bot, message(), "risposta")

        Mockito.verify(bot, Mockito.times(2)).executeAsync(captor?.capture())

        val risposta = captor.allValues[1]
        assertThat(risposta.chatId).isEqualTo("99")
        assertThat(risposta.text).isEqualTo("risposta")
        assertThat(risposta.replyToMessageId).isEqualTo(42)
        assertThat(risposta.parseMode).isEqualTo("Markdown")
        assertThat(risposta.disableWebPagePreview ?: null).isNull() // elvis needed because of auto unboxing
        assertThat(risposta.disableNotification ?: null).isNull()
        assertThat(risposta.replyMarkup).isNull()
        assertThat(risposta.entities).isNull()
        assertThat(risposta.allowSendingWithoutReply ?: null).isNull()
    }

    private fun message(): Message {
        return Message().apply {
            this.messageId = 42
            chat = Chat().apply {
                this.id = 99
            }
        }
    }
}